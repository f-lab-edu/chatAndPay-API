package com.chatandpay.api.dto

import com.chatandpay.api.common.ApiResponse
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.util.*
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class ChangePendingTransferResponseDTO(

    var isSucceed: Boolean,
    var remainPendingTransfer: Int,

) : ApiResponse()