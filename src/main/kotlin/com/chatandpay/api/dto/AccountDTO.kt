package com.chatandpay.api.dto

import com.chatandpay.api.common.ApiResponse
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

class AccountDTO {

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class OtherBankAccountRequestDTO(
        val bankCode: String,
        val accountNumber: String,
        val accountName: String,
        val autoDebitAgree: String,
        val userId: Long,
    )


    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class OtherBankAccountResponseDTO (
        val bankCode: String,
        val accountNumber: String,
        val accountName: String,
        val autoDebitAgree: String,
    ) : ApiResponse()


    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class DepositWalletRequestDTO (
        val depositMoney: Int,
        val accountId: Long,
    )


    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class DepositWalletResponseDTO (
        val chargeAmount : Int,
        val walletAmount : Int
    ) : ApiResponse()

}