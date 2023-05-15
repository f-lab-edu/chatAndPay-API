package com.chatandpay.api.service

import com.chatandpay.api.controller.dto.InquiryRealNameDTO
import com.chatandpay.api.controller.dto.OtherBankAccountRequestDTO
import com.chatandpay.api.domain.BANK_CODE
import com.chatandpay.api.domain.OtherBankAccount
import com.chatandpay.api.repository.AccountRepository
import com.chatandpay.api.repository.PayUserRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class AccountService (val accountRepository : AccountRepository, val payUserRepository: PayUserRepository, val openApiService: OpenApiService){

    @Transactional
    fun saveAccount(dto: OtherBankAccountRequestDTO) {

        val selectedUser = payUserRepository.findById(dto.userId) ?: throw IllegalArgumentException("존재하지 않는 유저입니다.")
        BANK_CODE.values().find { it.bankCode == dto.bankCode} ?: throw IllegalArgumentException("존재하지 않는 뱅크 코드입니다.")

        val account = OtherBankAccount(null, dto.bankCode, dto.accountNumber, dto.accountName, dto.autoDebitAgree, selectedUser)

        val inquiry = InquiryRealNameDTO("", dto.bankCode, dto.accountNumber, " ", "", "", dto.userId)
        val selectAccount = openApiService.getInquiryRealName(inquiry)

        if ( selectAccount.accountHolderName != selectedUser.user.name ) {
            throw IllegalArgumentException("조회 계좌와 고객명이 다릅니다.")
        }

        if ( accountRepository.findDuplicatedAccount(account) ) {
            throw IllegalArgumentException("기 등록 계좌입니다.")
        } else {
            accountRepository.save(account)
        }

    }


}