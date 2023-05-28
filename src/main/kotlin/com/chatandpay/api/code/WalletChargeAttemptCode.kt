package com.chatandpay.api.code

enum class WalletChargeAttemptCode(val value: Int) {
    TRY(0),
    API_SUCCESS(1),
    DB_SUCCESS(2),
    FAIL(-1)
}