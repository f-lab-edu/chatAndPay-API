package com.chatandpay.api.dto
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class OtherBankTransferRequestDTO(

    val senderId: Long,
    val bankCode: String,
    val accountNumber: String,
    val amount: Int

)