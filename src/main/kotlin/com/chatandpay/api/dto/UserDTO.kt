package com.chatandpay.api.dto

import com.chatandpay.api.common.ApiResponse
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class UserDTO (
    var name: String,
    var cellphone: String,
    var email: String? = null,
    var userId: String? = null,
    var password: String? = null,
    var verificationId: Long? = null,
) : ApiResponse()