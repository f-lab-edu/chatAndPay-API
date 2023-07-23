package com.chatandpay.api.dto

import com.chatandpay.api.common.ApiResponse
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import javax.validation.constraints.*

class UserDTO {

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class UserRequestDTO(

        @field:Size(min = 2, message = "이름은 2자리 이상 입력해 주세요.")
        @field:Pattern(regexp = "^[가-힣]*\$", message = "이름은 한글로만 입력해 주세요.")
        var name: String,

        @field:NotBlank(message = "휴대전화번호를 입력하세요.")
        @field:Size(min = 11, max = 11, message = "휴대전화번호는 11자리로 입력해주세요.")
        @field:Pattern(regexp = "^[1-9]*\$", message = "휴대전화번호는 숫자로만 입력해 주세요.")
        var cellphone: String,

        @field:Positive(message = "Verification ID 미입력")
        var verificationId: Long,

        @field:Email(message = "올바른 이메일 형식이 아닙니다.")
        var email: String? = null,

        @field:Size(min = 5, max = 15, message = "아이디는 5-15자 사이로 입력해주세요.")
        var userId: String? = null,

        @field:Size(min = 8, max = 16, message = "비밀번호는 8-16자 사이로 입력해주세요.")
        var password: String? = null
        
    )

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class UserResponseDTO(

        var name: String,
        var cellphone: String,
        var email: String? = null,
        var userId: String? = null,
        var accessToken: String? = null

    ) : ApiResponse()


    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class UserLoginRequestDTO(

        @field:NotBlank(message = "ID를 입력하세요.")
        var userId: String,

        @field:NotBlank(message = "패스워드를 입력하세요.")
        var password: String,

    )

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class UserSMSRequestDTO(

        @field:NotBlank(message = "휴대전화번호를 입력하세요.")
        @field:Size(min = 11, max = 11, message = "휴대전화번호는 11자리로 입력해주세요.")
        @field:Pattern(regexp = "^[1-9]*\$", message = "휴대전화번호는 숫자로만 입력해 주세요.")
        var cellphone: String,

    )


    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class AuthConfirmRequestDTO(

        @field:NotBlank(message = "휴대전화번호를 입력하세요.")
        @field:Size(min = 11, max = 11, message = "휴대전화번호는 11자리로 입력해주세요.")
        @field:Pattern(regexp = "^[1-9]*\$", message = "휴대전화번호는 숫자로만 입력해 주세요.")
        var cellphone: String,

        @field:NotBlank(message = "인증번호를 입력하세요.")
        @field:Size(min = 6, max = 6, message = "인증번호는 6자리로 입력해주세요.")
        @field:Pattern(regexp = "^[1-9]*\$", message = "휴대전화번호는 숫자로만 입력해 주세요.")
        val authNumber: String

    )

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class AuthConfirmResponseDTO(
        val verificationId: Long
    ) : ApiResponse()

    data class AuthTokenUserRequestDTO(

        @field:NotBlank(message = "이메일을 입력하세요.")
        @field:Email(message = "올바른 이메일 형식이 아닙니다.")
        val email: String

    )

    data class AuthLoginUserRequestDTO(

        @field:NotBlank(message = "휴대전화번호를 입력하세요.")
        @field:Size(min = 11, max = 11, message = "휴대전화번호는 11자리로 입력해주세요.")
        @field:Pattern(regexp = "^[1-9]*\$", message = "휴대전화번호는 숫자로만 입력해 주세요.")
        val cellphone: String

    )

    data class ValidRefreshTokenResponse(
        val userPk: String?,
        val accessToken: String?,
        val refreshToken: String?,
    ): ApiResponse()

    data class TokenInfo(
        val accessToken: String,
        val refreshToken: String,
    )

}