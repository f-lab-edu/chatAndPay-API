package com.chatandpay.api.dto

import com.chatandpay.api.domain.PayUser
data class OpenApiDepositWalletDTO (
    val depositMoney: Int,
    val accountId: Long,
    val payUser: PayUser,
)