package com.chatandpay.api.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.util.*
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class ChangePendingTransferRequestDTO(

    val uuid: UUID,
    val requestUserId: Long,
    val changeType: String,

)