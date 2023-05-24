package com.chatandpay.api.dto

import com.chatandpay.api.common.ApiResponse
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class SendTransferResponseDTO (

    @JsonProperty("transfer_uuid")
    val transferUuid: UUID,

    @JsonProperty("received_amount")
    val receivedAmount: Int,

    @JsonProperty("wallet_amount")
    val walletAmount: Int,


) : ApiResponse()