package com.chatandpay.api.dto

import java.util.*

data class PendingTransferDTO (

    val uuid: UUID,
    val sender: Long,
    val receiver: Long,
    val amount: Int

)