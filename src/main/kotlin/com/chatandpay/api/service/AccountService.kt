package com.chatandpay.api.service

import com.chatandpay.api.domain.OtherBankAccount
import com.chatandpay.api.dto.OpenApiDepositWalletDTO
import com.chatandpay.api.repository.AccountRepository
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
class AccountService (
    val accountRepository : AccountRepository,
    val payUserService: PayUserService,
){

    @Transactional
    fun saveAccount(dto: OtherBankAccount): OtherBankAccount? {

        return accountRepository.save(dto)

    }


    @Transactional
    fun chargeWallet(dto: OpenApiDepositWalletDTO) : Int? {

        return payUserService.depositintoWallet(dto.depositMoney, dto.payUser)?.wallet?.money

    }


}