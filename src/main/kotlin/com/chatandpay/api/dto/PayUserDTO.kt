package com.chatandpay.api.dto

import com.chatandpay.api.common.ApiResponse
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import javax.validation.constraints.Pattern
import javax.validation.constraints.Positive
import javax.validation.constraints.Size


class PayUserDTO {

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class SignUpRequestPayUserDTO(

        @field:Pattern(regexp = "^[A-Za-z0-9]*$", message = "Input Error: 영어/숫자 대응 필요")
        @field:Size(min = 88, max = 88, message = "Input Error: 자릿수 상이 - 88바이트")
        val ci: String,
        @field:Positive(message = "Input Error: userId 미입력")
        val userId: Long,
        @field:Pattern(regexp = "^(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])$", message = "유효한 생년월일 형식이 아닙니다.")
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
