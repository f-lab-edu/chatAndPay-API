package com.chatandpay.api.service


import com.chatandpay.api.domain.Transfer
import com.chatandpay.api.dto.*
import com.chatandpay.api.repository.PayUserRepository
import com.chatandpay.api.repository.TransferRepository
import com.chatandpay.api.repository.WalletRepository
import org.springframework.stereotype.Service
import java.util.*
import javax.persistence.EntityNotFoundException
import javax.transaction.Transactional

@Service
class TransferService (
    private val payUserRepository: PayUserRepository,
    private val walletRepository : WalletRepository,
    private val transferRepository: TransferRepository,
    private val payUserService : PayUserService
){

    @Transactional
    fun sendTransfer(dto: ReceiveTransferRequestDTO) : ReceiveTransferResponseDTO? {

        val findSendUser = payUserRepository.findById(dto.senderId) ?: throw EntityNotFoundException("송신자를 찾을 수 없습니다.")
        val findReceiveUser = payUserRepository.findById(dto.receiverId) ?: throw EntityNotFoundException("수신자를 찾을 수 없습니다.")
        val findSenderWallet = findSendUser.wallet ?: throw EntityNotFoundException("송신자의 지갑을 찾을 수 없습니다.")
        val findSenderWalletAmount = findSenderWallet.money

        if (findSenderWalletAmount < dto.amount) {
            throw IllegalArgumentException("출금 잔액이 부족합니다.")
        }

        findSenderWallet.money = findSenderWallet.money - dto.amount
        walletRepository.save(findSenderWallet)

        val transferDto = Transfer(UUID.randomUUID(), findSendUser, findReceiveUser, dto.amount, false)
        val savedTransfer = transferRepository.save(transferDto)

        return savedTransfer?.let {
            ReceiveTransferResponseDTO(
                transferUuid = it.uuid,
                sendingAmount = it.amount,
                walletAmount = findSendUser.wallet?.money ?: throw EntityNotFoundException("송신자의 지갑을 찾을 수 없습니다.")
            )
        }
    }


    @Transactional
    fun receiveTransfer(uuid: UUID): SendTransferResponseDTO {

        val findTransferObj = transferRepository.findById(uuid) ?: throw EntityNotFoundException("송신 요청을 찾을 수 없습니다.")

        val payUser = findTransferObj.receiver
        val payUserId = payUser.id ?: throw EntityNotFoundException("송신 요청을 찾을 수 없습니다.")

        if (findTransferObj.transferred) {
            throw IllegalArgumentException("이미 수신 완료된 송신 요청입니다.")
        }

        val inputDTO = DepositWalletDTO(findTransferObj.amount, payUserId)

        val calculatedMoney = (payUser.wallet?.money ?: 0) + findTransferObj.amount
        val outputMoney = payUserService.depositintoWallet(inputDTO, payUser)?.wallet?.money
            ?: throw EntityNotFoundException("금액 수신에 실패했습니다.")

        if (calculatedMoney == outputMoney) {
            findTransferObj.transferred = true
        }

        val savedTransfer = transferRepository.save(findTransferObj)
        val amount = savedTransfer?.receiver?.wallet?.money ?: throw EntityNotFoundException("금액 수신에 실패했습니다.")

        return SendTransferResponseDTO(savedTransfer.uuid, outputMoney, amount)
    }



}