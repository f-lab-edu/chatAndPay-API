package com.chatandpay.api.service

import com.chatandpay.api.dto.InquiryRealNameDTO
import com.chatandpay.api.dto.OtherBankAccountRequestDTO
import com.chatandpay.api.code.BankCode
import com.chatandpay.api.domain.OtherBankAccount
import com.chatandpay.api.repository.AccountRepository
import com.chatandpay.api.repository.PayUserRepository
import com.chatandpay.api.dto.DepositWalletDTO
import org.springframework.stereotype.Service
import javax.persistence.EntityNotFoundException
import javax.transaction.Transactional

@Service
class AccountService (
        val accountRepository : AccountRepository,
        val payUserRepository: PayUserRepository,
        val openApiService: OpenApiService,
        val payUserService: PayUserService,
){

    @Transactional
    fun saveAccount(dto: OtherBankAccountRequestDTO) {

        val selectedUser = payUserRepository.findById(dto.userId) ?: throw IllegalArgumentException("존재하지 않는 유저입니다.")
        BankCode.values().find { it.bankCode == dto.bankCode } ?: throw IllegalArgumentException("존재하지 않는 뱅크 코드입니다.")

        val account = OtherBankAccount(null, dto.bankCode, dto.accountNumber, dto.accountName, dto.autoDebitAgree, selectedUser)

        val inquiry = InquiryRealNameDTO("", dto.bankCode, dto.accountNumber, " ", "", "", dto.userId)
        val selectAccount = openApiService.getInquiryRealName(inquiry)

        if ( selectAccount.accountHolderName != selectedUser.user.name ) {
            throw IllegalArgumentException("조회 계좌와 고객명이 다릅니다.")
        }

        if ( accountRepository.findDuplicatedAccount(account) ) {
            throw IllegalArgumentException("기 등록 계좌입니다.")
        }

        accountRepository.save(account)

    }


    @Transactional
    fun chargeWallet(depositWallet : DepositWalletDTO) : Int? {

        val user = accountRepository.findById(depositWallet.accountId)?.payUser ?: throw EntityNotFoundException("대외 계좌 IDX 입력 오류")
        val updatesMoney = depositWallet.depositMoney
        // DB에 선저장 처리 - DB 에러 선 방지, 이후 API 오류가 나더라도 Transactional로 처리
        val outputMoney = payUserService.depositintoWallet(updatesMoney, user)?.wallet?.money
        val depositedMoney = outputMoney?.let { openApiService.withdrawMoney(it) }

        // DB에 저장된 output 값 == API 호출 후 전달 값 비교
        if (depositedMoney != outputMoney) {
            throw RuntimeException("충전 실패")
        }

        return depositedMoney
    }


}