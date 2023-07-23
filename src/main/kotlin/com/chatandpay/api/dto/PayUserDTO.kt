package com.chatandpay.api.dto

import com.chatandpay.api.common.ApiResponse
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming


class PayUserDTO {

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class SignUpRequestPayUserDTO(
        val ci: String,
        val userId: Long,
        val userSeqNo: String,
        val birthDate: String,
    )

    data class SignUpResponsePayUserDTO (

        var name: String,
        var cellphone: String,
        val payUserId: Long,
        val userSeqNo: String,
        val birthDate: String

    ) : ApiResponse()


}
