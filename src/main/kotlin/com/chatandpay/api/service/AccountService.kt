package com.chatandpay.api.service

import com.chatandpay.api.domain.OtherBankAccount
import com.chatandpay.api.domain.PayUser
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
    fun chargeWallet(updatesMoney : Int, user: PayUser) : Int? {

        return payUserService.depositintoWallet(updatesMoney, user)?.wallet?.money

    }


}