package com.chatandpay.api.dto

import com.chatandpay.api.common.ApiResponse
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class OtherBankAccountResponseDTO (
    val bankCode: String,
    val accountNumber: String,
    val accountName: String,
    val autoDebitAgree: String,
) : ApiResponse()