package com.chatandpay.api.service

import com.chatandpay.api.dto.SignUpPayUserDTO
import com.chatandpay.api.domain.PayUser
import com.chatandpay.api.domain.Wallet
import com.chatandpay.api.dto.PayUserDTO
import com.chatandpay.api.repository.PayUserRepository
import com.chatandpay.api.repository.UserRepository
import com.chatandpay.api.repository.WalletRepository
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException
import javax.transaction.Transactional

@Service
class PayUserService(
    private val payUserRepository: PayUserRepository,
    private val userRepository: UserRepository,
    private val walletRepository : WalletRepository
) {

    @Transactional
    fun register(payUser : SignUpPayUserDTO): PayUserDTO? {

        if(payUserRepository.findByCi(payUser.ci) != null) {
            throw IllegalArgumentException("기 가입 유저입니다.")
        }

        val user = userRepository.findById(payUser.userId) ?: throw IllegalArgumentException("존재하지 않는 유저입니다.")

        val regUser = PayUser(ci = payUser.ci, user = user, userSeqNo = payUser.userSeqNo, wallet = null, birthDate = payUser.birthDate)
        val savedUser = payUserRepository.save(regUser) ?: throw RuntimeException("페이 회원 가입에 실패하였습니다.")
        val wallet = Wallet(money = 0, payUser = savedUser)
        savedUser.wallet = walletRepository.save(wallet) ?: throw RuntimeException("페이 회원 가입에 실패하였습니다.")

        return savedUser.id?.let { PayUserDTO(user.name, user.cellphone, it, savedUser.userSeqNo, savedUser.birthDate) }

    }

    @Transactional
    fun withdrawPayService(userId: Long) : Boolean {

        val findUser = payUserRepository.findByUserId(userId) ?: throw EntityNotFoundException("IDX 입력이 잘못되었습니다.")
        val findUserWallet = findUser.id?.let { walletRepository.findByPayUserId(it) }

        try {
            if (findUserWallet != null) {
                walletRepository.delete(findUserWallet)
            }
            return payUserRepository.delete(findUser)

        } catch (e : Exception) {
            throw RuntimeException(e.message)
            // TODO 이후 브랜치 머지 시 RestApiException 변경
        }

    }

    fun isPayUser(userId: Long): Boolean {
        return payUserRepository.findByUserId(userId) != null
    }


    fun depositintoWallet(updatesMoney : Int, user: PayUser) : PayUser? {
        val currentMoney = user.wallet?.money ?: throw EntityNotFoundException("IDX 입력이 잘못되었습니다.")
        user.wallet?.money = currentMoney + updatesMoney
        return payUserRepository.save(user)
    }


}