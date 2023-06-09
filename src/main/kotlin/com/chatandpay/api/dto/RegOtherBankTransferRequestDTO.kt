package com.chatandpay.api.dto
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class RegOtherBankTransferRequestDTO(

    val senderId: Long,
    val senderBankAccount: Long,
    val amount: Int

)