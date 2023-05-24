package com.chatandpay.api.dto

import com.chatandpay.api.common.ApiResponse
import com.fasterxml.jackson.annotation.JsonProperty


data class ReceiveTransferRequestDTO(

    @JsonProperty("sender_id")
    val senderId: Long,

    @JsonProperty("receiver_id")
    val receiverId: Long,

    val amount: Int

): ApiResponse()


