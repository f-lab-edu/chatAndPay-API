package com.chatandpay.api.component

import com.chatandpay.api.code.BankCode
import com.chatandpay.api.domain.OtherBankAccount
import com.chatandpay.api.dto.DepositWalletDTO
import com.chatandpay.api.dto.InquiryRealNameDTO
import com.chatandpay.api.dto.OtherBankAccountRequestDTO
import com.chatandpay.api.repository.AccountRepository
import com.chatandpay.api.repository.PayUserRepository
import com.chatandpay.api.service.AccountService
import com.chatandpay.api.service.OpenApiService
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Component
import javax.persistence.EntityNotFoundException
@RequiredArgsConstructor
@Component
class AccountCheck  (
    val accountRepository : AccountRepository,
    val payUserRepository: PayUserRepository,
    val openApiService: OpenApiService,
    val accountService: AccountService,
){

    fun chargeWallet(dto : DepositWalletDTO) : Int? {

        val user = accountRepository.findById(dto.accountId)?.payUser
                ?: throw EntityNotFoundException("대외 계좌 IDX 입력 오류")
        val outputMoney = openApiService.withdrawMoney(dto.depositMoney)

        return accountService.chargeWallet(outputMoney, user)

    }

    fun saveAccount(dto: OtherBankAccountRequestDTO) : OtherBankAccount? {

        val inquiry = InquiryRealNameDTO("", dto.bankCode, dto.accountNumber, " ", "", "", dto.userId)
        val selectAccount = openApiService.getInquiryRealName(inquiry)

        val selectedUser = payUserRepository.findById(dto.userId) ?: throw IllegalArgumentException("존재하지 않는 유저입니다.")
        BankCode.values().find { it.bankCode == dto.bankCode} ?: throw IllegalArgumentException("존재하지 않는 뱅크 코드입니다.")

        val account = OtherBankAccount(null, dto.bankCode, dto.accountNumber, dto.accountName, dto.autoDebitAgree, selectedUser)

        if ( selectAccount.accountHolderName != selectedUser.user.name ) {
            throw IllegalArgumentException("조회 계좌와 고객명이 다릅니다.")
        }

        if ( accountRepository.findDuplicatedAccount(account) ) {
            throw IllegalArgumentException("기 등록 계좌입니다.")
        }

        return accountService.saveAccount(account)
    }


}