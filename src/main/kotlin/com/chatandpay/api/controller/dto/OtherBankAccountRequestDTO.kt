package com.chatandpay.api.controller.dto

data class OtherBankAccountRequestDTO(
    val bankCode: String,
    val accountNumber: String,
    val accountName: String,
    val autoDebitAgree: String,
    val userId: Long,
)