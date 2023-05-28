package com.chatandpay.api.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class AuthConfirmRequestDTO (
    val name : String?,
    val cellphone: String,
    val authNumber : String?,
)
