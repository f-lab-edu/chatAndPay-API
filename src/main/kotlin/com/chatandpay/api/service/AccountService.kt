package com.chatandpay.api.service

import com.chatandpay.api.controller.dto.OtherBankAccountRequestDTO
import com.chatandpay.api.domain.BANK_CODE
import com.chatandpay.api.domain.OtherBankAccount
import com.chatandpay.api.repository.AccountRepository
import com.chatandpay.api.repository.PayUserRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class AccountService (val accountRepository : AccountRepository, val payUserRepository: PayUserRepository){

    @Transactional
    fun saveAccount(dto: OtherBankAccountRequestDTO) {

        val userId = payUserRepository.findById(dto.userId) ?: throw IllegalArgumentException("존재하지 않는 유저입니다.")
        BANK_CODE.values().find { it.bankCode == dto.bankCode} ?: throw IllegalArgumentException("존재하지 않는 뱅크 코드입니다.")

        val account = OtherBankAccount(null, dto.bankCode, dto.accountNumber, dto.accountName, dto.autoDebitAgree, userId)

        if ( accountRepository.findDuplicatedAccount(account) ) {
            throw IllegalArgumentException("기 등록 계좌입니다.")
        } else {
            accountRepository.save(account)
        }

    }


}