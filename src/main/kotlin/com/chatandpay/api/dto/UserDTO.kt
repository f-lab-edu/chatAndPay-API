package com.chatandpay.api.dto

import com.chatandpay.api.common.ApiResponse
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

class UserDTO {

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class UserRequestDTO (
        var name: String,
        var cellphone: String,
        var verificationId: Long,
        var email: String? = null,
        var userId: String? = null,
        var password: String? = null,
    )

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class UserResponseDTO (
        var name: String,
        var cellphone: String,
        var email: String? = null,
        var userId: String? = null,
        var accessToken: String? = null
    ) : ApiResponse()

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class AuthConfirmRequestDTO (
        val name : String?,
        val cellphone: String,
        val authNumber : String?,
    )


    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class AuthConfirmResponseDTO (
        val verificationId: Long
    )  : ApiResponse()



    data class AuthLoginUserRequestDTO (
        val cellphone: String?,
        val email: String?
    )

}