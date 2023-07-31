package com.chatandpay.api.service

import com.chatandpay.api.domain.OtherBankAccount
import com.chatandpay.api.dto.OtherBankAccountListResponseDTO
import com.chatandpay.api.dto.OtherBankAccountRawResponseDTO
import com.chatandpay.api.dto.OpenApiDTO
import com.chatandpay.api.exception.RestApiException
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
    fun chargeWallet(dto: OpenApiDTO.OpenApiDepositWalletDTO) : OpenApiDTO.OpenApiDepositWalletDTO {

        val payUser = payUserService.depositintoWallet(dto.depositMoney, dto.payUser)
            ?: throw RestApiException()
        return OpenApiDTO.OpenApiDepositWalletDTO(dto.depositMoney, dto.accountId, payUser)

    }

    fun getExternalAccount(userId: Long): OtherBankAccountListResponseDTO {

        val accountList = accountRepository.findByPayUserId(userId).map {
            OtherBankAccountRawResponseDTO(it.bankCode, it.accountNumber, it.accountName, it.autoDebitAgree)
        }

        return OtherBankAccountListResponseDTO(accountList)

    }


}