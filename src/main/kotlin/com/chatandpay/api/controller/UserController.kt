package com.chatandpay.api.controller

import com.chatandpay.api.code.ErrorCode
import com.chatandpay.api.common.ApiResponse
import com.chatandpay.api.common.ErrorResponse
import com.chatandpay.api.common.JwtTokenProvider
import com.chatandpay.api.common.SuccessResponse
import com.chatandpay.api.dto.*
import com.chatandpay.api.exception.RestApiException
import com.chatandpay.api.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse


@RestController
@RequestMapping("/users")
class UserController(
    val userService: UserService,
    val jwtTokenProvider: JwtTokenProvider
) {

    @PostMapping("/login")
    fun loginUser(@RequestBody user: UserDTO, response: HttpServletResponse) : ResponseEntity<UserDTO>{

        val loginUser = userService.login(user)
        val tokens = loginUser?.id?.let { userService.tokenLoginUser(it) }
        val responseBody = loginUser?.let { UserDTO(name = it.name, cellphone = it.cellphone, userId = it.userId, accessToken = tokens?.accessToken) }

        val accessTokenCookie = Cookie(JwtTokenProvider.ACCESS_TOKEN_NAME, tokens?.accessToken)
        val refreshTokenCookie = Cookie(JwtTokenProvider.REFRESH_TOKEN_NAME, tokens?.refreshToken)

        response.addCookie(accessTokenCookie)
        response.addCookie(refreshTokenCookie)

        return ResponseEntity.ok(responseBody)


    }


    @PostMapping("/token/login")
    fun tokenLoginUser(@RequestBody requestBody: AuthLoginUserRequestDTO, response: HttpServletResponse) : ResponseEntity<UserDTO>{

        val loginUser = requestBody.email?.let { userService.emailLogin(it) }
        val tokens = requestBody.email?.let { userService.tokenLoginUser(it) }
        val responseBody = loginUser?.let { UserDTO(name = it.name, cellphone = it.cellphone, email = it.email, accessToken = tokens?.accessToken) }

        val accessTokenCookie = Cookie(JwtTokenProvider.ACCESS_TOKEN_NAME, tokens?.accessToken)
        val refreshTokenCookie = Cookie(JwtTokenProvider.REFRESH_TOKEN_NAME, tokens?.refreshToken)

        response.addCookie(accessTokenCookie)
        response.addCookie(refreshTokenCookie)

        return ResponseEntity.ok(responseBody)

    }

    @PostMapping("/token/refresh")
    fun tokenRefresh(
        response: HttpServletResponse,
        @CookieValue(value = "accessToken") accessToken: String,
        @CookieValue(value = "refreshToken") refreshToken: String,
    ) : ResponseEntity<ValidRefreshTokenResponse>{

        val tokens = jwtTokenProvider.validateRefreshToken(accessToken, refreshToken)

        val accessTokenCookie = Cookie(JwtTokenProvider.ACCESS_TOKEN_NAME, tokens.accessToken)

        response.addCookie(accessTokenCookie)

        return ResponseEntity.ok(tokens)

    }

    @PostMapping("/auth")
    fun authLoginUser(@RequestBody requestBody: AuthLoginUserRequestDTO) : ResponseEntity<UserDTO>{

        val authLoginUser = requestBody.cellphone?.let { userService.authLogin(it) }
        val responseBody = authLoginUser?.let { UserDTO(name = it.name, cellphone = it.cellphone, userId = it.userId) }

        return ResponseEntity.ok(responseBody)

    }

    @PostMapping("/auth/signup")
    fun authJoinUser(@RequestBody user: UserDTO) : ResponseEntity<SuccessResponse>{

        userService.authJoin(user) ?: throw RestApiException("인증 요청 중 오류가 발생하였습니다.")

        val responseBody = SuccessResponse("메시지 발송 성공")

        return ResponseEntity.ok(responseBody)

    }

    @PostMapping("/auth/confirm")
    fun confirmAuthLoginUser(@RequestBody saveObj : AuthConfirmRequestDTO, response: HttpServletResponse) : ResponseEntity<UserDTO>{

        val confirmedUser = userService.authLoginConfirm(saveObj)

        val tokens = confirmedUser?.id?.let { userService.tokenLoginUser(it) }

        val accessTokenCookie = Cookie(JwtTokenProvider.ACCESS_TOKEN_NAME, tokens?.accessToken)
        val refreshTokenCookie = Cookie(JwtTokenProvider.REFRESH_TOKEN_NAME, tokens?.refreshToken)

        response.addCookie(accessTokenCookie)
        response.addCookie(refreshTokenCookie)

        val responseBody = confirmedUser?.let { UserDTO(name = it.name, cellphone = it.cellphone, userId = it.userId, accessToken = tokens?.accessToken) }

        return ResponseEntity.ok(responseBody)

    }


    @PostMapping("/auth/signup/confirm")
    fun confirmAuthJoinUser(@RequestBody saveObj : AuthConfirmRequestDTO) : ResponseEntity<AuthConfirmResponseDTO>{

        val smsAuthentication = userService.authConfirm(saveObj)

        val responseBody = smsAuthentication.id?.let { AuthConfirmResponseDTO(it) }

        return ResponseEntity.ok(responseBody)

    }

    @PostMapping("/signup")
    fun createUser(@RequestBody user: UserDTO) : ResponseEntity<UserDTO>{

        val signupUser = userService.register(user)
        val responseBody = signupUser?.let { UserDTO(name = it.name, cellphone = it.cellphone, userId = it.userId) }

        return ResponseEntity.ok(responseBody)

    }

    @PatchMapping("/{id}")
    fun updateUser(@PathVariable("id") id: Long, @RequestBody userRequest: UserDTO) : ResponseEntity<UserDTO>{

        val updatedUser = userService.updateUser(id, userRequest)
        val responseBody = updatedUser?.let { UserDTO(name = it.name, cellphone = it.cellphone, userId = it.userId) }

        return ResponseEntity.ok(responseBody)

    }


    @DeleteMapping("/{id}")
    fun deleteUser(@PathVariable("id") id: Long): ResponseEntity<ApiResponse>{

        val deletedYn = userService.deleteUser(id)

        return if (deletedYn) {
            val successResponse = SuccessResponse("탈퇴 완료")
            ResponseEntity.ok(successResponse)
        } else {
            val errorCode = ErrorCode.BAD_REQUEST
            val errorResponse = ErrorResponse(errorCode.value, "탈퇴 실패")
            ResponseEntity.badRequest().body(errorResponse)
        }

    }

}