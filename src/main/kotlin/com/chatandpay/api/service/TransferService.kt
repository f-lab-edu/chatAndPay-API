package com.chatandpay.api.service


import com.chatandpay.api.code.BankCode
import com.chatandpay.api.domain.OtherBankTransfer
import com.chatandpay.api.domain.Transfer
import com.chatandpay.api.domain.Wallet
import com.chatandpay.api.dto.*
import com.chatandpay.api.exception.RestApiException
import com.chatandpay.api.repository.AccountRepository
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
    private val accountRepository: AccountRepository,
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

        val transferDto = Transfer(UUID.randomUUID(), findSendUser, findReceiveUser, dto.amount, false, "I")
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

        if (findTransferObj.transferred) {
            throw IllegalArgumentException("이미 수신 완료된 송신 요청입니다.")
        }

        val updatesMoney = findTransferObj.amount
        val calculatedMoney = (payUser.wallet?.money ?: 0) + findTransferObj.amount

        val outputMoney = payUserService.depositintoWallet(updatesMoney, payUser)?.wallet?.money
            ?: throw EntityNotFoundException("금액 수신에 실패했습니다.")

        if (calculatedMoney == outputMoney) {
            findTransferObj.transferred = true
        }

        val savedTransfer = transferRepository.save(findTransferObj)
        val amount = savedTransfer?.receiver?.wallet?.money ?: throw EntityNotFoundException("금액 수신에 실패했습니다.")

        return SendTransferResponseDTO(savedTransfer.uuid, outputMoney, amount)
    }


    @Transactional
    fun sendMyOtherBankTransfer(dto: RegOtherBankTransferRequestDTO) : OtherBankTransferResponseDTO? {

        val findReceiveAccount = accountRepository.findById(dto.senderBankAccount) ?: throw EntityNotFoundException("수신 계좌를 찾을 수 없습니다.")

        if (findReceiveAccount.payUser.id != dto.senderId) {
            throw IllegalArgumentException("수신 계좌의 명의가 송신자의 계좌와 다릅니다.")
        }

        val otherBankTransferRequest = OtherBankTransferRequestDTO(dto.senderId, findReceiveAccount.bankCode, findReceiveAccount.accountNumber, dto.amount)

        return sendOtherBankTransfer(otherBankTransferRequest)
    }


    @Transactional
    fun sendOtherBankTransfer(dto: OtherBankTransferRequestDTO) : OtherBankTransferResponseDTO? {

        val findSendUser = payUserRepository.findById(dto.senderId) ?: throw EntityNotFoundException("송신자를 찾을 수 없습니다.")
        val findSenderWallet = findSendUser.wallet ?: throw EntityNotFoundException("송신자의 지갑을 찾을 수 없습니다.")
        val findSenderWalletAmount = findSenderWallet.money

        if (findSenderWalletAmount < dto.amount) {
            throw IllegalArgumentException("출금 잔액이 부족합니다.")
        }

        BankCode.values().find { it.bankCode == dto.bankCode } ?: throw IllegalArgumentException("존재하지 않는 뱅크 코드입니다.")
        val transferDto = OtherBankTransfer(UUID.randomUUID(), findSendUser, dto.bankCode, dto.accountNumber, dto.amount, false,"O")

        try {
            return finalizeOtherBankTransfer(transferDto, findSenderWallet)?.let {
                OtherBankTransferResponseDTO(
                    transferUuid = it.uuid,
                    isSucceeded = true,
                    sendingAmount = it.amount,
                    walletAmount = findSendUser.wallet?.money ?: throw EntityNotFoundException("송신자의 지갑을 찾을 수 없습니다.")
                )
            }

        } catch (e: Exception){
            throw RestApiException(e.message)
        }

    }



    @Transactional
    fun finalizeOtherBankTransfer(transferDto: OtherBankTransfer, findSenderWallet: Wallet) : OtherBankTransfer? {
        // TODO 통신부 => 외부 통신 후 실제 송금 완료 처리 부분
        transferDto.transferred = true
        findSenderWallet.money = findSenderWallet.money - transferDto.amount
        walletRepository.save(findSenderWallet)
        return transferRepository.save(transferDto)
    }



}