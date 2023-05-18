package com.chatandpay.api.dto

import com.chatandpay.api.common.ApiResponse

class UserDTO (
    var name: String,
    var cellphone: String,
    var userId: String?
) : ApiResponse()