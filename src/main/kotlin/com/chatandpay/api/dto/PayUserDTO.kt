package com.chatandpay.api.dto

import com.chatandpay.api.common.ApiResponse

data class PayUserDTO (

    var name: String,
    var cellphone: String,
    val payUserId: Long,
    val userSeqNo: String,
    val birthDate: String,

) : ApiResponse()
