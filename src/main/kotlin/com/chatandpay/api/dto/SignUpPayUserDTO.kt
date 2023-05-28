package com.chatandpay.api.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class SignUpPayUserDTO(
    val ci: String,
    val userId: Long,
    val userSeqNo: String,
    val birthDate: String,
)