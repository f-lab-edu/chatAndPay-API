package com.chatandpay.api.service

import com.chatandpay.api.code.ErrorCode
import com.chatandpay.api.domain.PayUser
import com.chatandpay.api.domain.Wallet
import com.chatandpay.api.dto.PayUserDTO
import com.chatandpay.api.exception.RestApiException
import com.chatandpay.api.repository.*
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException
import javax.transaction.Transactional

@Service
class PayUserService(
    private val payUserRepository: PayUserRepository,
    private val userRepository: UserRepository,
    private val walletRepository : WalletRepository,
    private val transferRepository: TransferRepository,
    private val accountRepository: AccountRepository
) {

    @Transactional
    fun register(payUser: PayUserDTO.SignUpRequestPayUserDTO): PayUserDTO.SignUpResponsePayUserDTO? {

        val findUser = payUserRepository.findByCi(payUser.ci)

        if(findUser != null && !findUser.isWithdrawn) {
            throw IllegalArgumentException("기 가입 유저입니다.")
        }

        val user = userRepository.findById(payUser.userId) ?: throw IllegalArgumentException("존재하지 않는 유저입니다.")

        val regUser = if(findUser?.isWithdrawn == true){
            findUser.withdrawnYn = "N"
            findUser
        } else {
            PayUser(ci = payUser.ci, user = user, wallet = null, birthDate = payUser.birthDate)
        }

        val savedUser = payUserRepository.save(regUser) ?: throw RestApiException("페이 회원 가입에 실패하였습니다.")
        val wallet = Wallet(money = 0, payUser = savedUser)
        savedUser.wallet = walletRepository.save(wallet) ?: throw RestApiException("페이 회원 가입에 실패하였습니다.")

        return savedUser.id?.let { PayUserDTO.SignUpResponsePayUserDTO(user.name, user.cellphone, it, savedUser.userSeqNo, savedUser.birthDate) }

    }

    @Transactional
    fun withdrawPayService(ulid: String) : Boolean {

        val findUserId = userRepository.findByUlid(ulid)?.id ?: throw EntityNotFoundException("ulid 입력이 잘못되었습니다.")
        return this.withdrawPayService(findUserId)

    }

    @Transactional
    fun withdrawPayService(userId: Long) : Boolean {

        val findUser = payUserRepository.findByUserId(userId) ?: throw EntityNotFoundException("IDX 입력이 잘못되었습니다.")
        val findUserWallet = findUser.id?.let { walletRepository.findByPayUserId(it) }
        val countUndoneTransfer = transferRepository.findPendingTransfers(findUser)

        if(countUndoneTransfer.isNotEmpty()) {
            throw RestApiException("송금이 완료되지 않은 거래가 존재합니다.", ErrorCode.BAD_REQUEST)
        }

        try {
            if (findUserWallet != null) {

                if(findUserWallet.money > 0) {
                    throw RestApiException("지갑에 잔액이 존재합니다.", ErrorCode.BAD_REQUEST)
                }

                val isWalletRemoved = walletRepository.delete(findUserWallet)
                if(isWalletRemoved) {
                    findUser.user = null
                    findUser.wallet = null
                    findUser.withdrawnYn = "Y"

                    accountRepository.deleteAllAccount(findUser.id!!)
                    return payUserRepository.delete(findUser)
                }
            }

            return false

        } catch (e : Exception) {
            throw RestApiException(e.message)
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