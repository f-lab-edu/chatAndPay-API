package com.chatandpay.api.dto

import com.chatandpay.api.common.ApiResponse
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class ReceiveTransferResponseDTO(

    @JsonProperty("transfer_uuid")
    val transferUuid: UUID,

    @JsonProperty("sending_amount")
    val sendingAmount: Int,

    @JsonProperty("wallet_amount")
    val walletAmount: Int


) : ApiResponse()
