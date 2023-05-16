package com.chatandpay.api.dto

data class SignUpPayUserDTO(
    val ci: String,
    val userId: Long,
    val userSeqNo: String,
    val birthDate: String,
)