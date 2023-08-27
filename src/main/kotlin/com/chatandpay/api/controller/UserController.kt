package com.chatandpay.api.controller

import com.chatandpay.api.code.ErrorCode
import com.chatandpay.api.common.ApiResponse
import com.chatandpay.api.common.ErrorResponse
import com.chatandpay.api.common.JwtTokenProvider
import com.chatandpay.api.common.SuccessResponse
import com.chatandpay.api.dto.*
import com.chatandpay.api.exception.RestApiException
import com.chatandpay.api.service.UserService
import com.chatandpay.api.utils.CookieUtil
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid

@RestController
@RequestMapping("/users")
class UserController(
    val userService: UserService,
    val jwtTokenProvider: JwtTokenProvider
) {

    @PostMapping("/login")
    fun loginUser(@RequestBody @Valid user: UserDTO.UserLoginRequestDTO, response: HttpServletResponse) : ResponseEntity<UserDTO.UserResponseDTO>{

        val loginUser = userService.login(user)
        val tokens = loginUser?.id?.let { userService.tokenLoginUser(it) }
        val responseBody = loginUser?.let { UserDTO.UserResponseDTO(ulid = it.ulid, name = it.name, cellphone = it.cellphone, userId = it.userId, accessToken = tokens?.accessToken) }

        CookieUtil.addTokenCookies(tokens?.accessToken, tokens?.refreshToken, response)

        return ResponseEntity.ok(responseBody)


    }


    @PostMapping("/token/login")
    fun tokenLoginUser(@RequestBody @Valid requestBody: UserDTO.AuthTokenUserRequestDTO, response: HttpServletResponse) : ResponseEntity<UserDTO.UserResponseDTO>{

        val loginUser = userService.emailLogin(requestBody.email)
        val tokens = loginUser?.id?.let { userService.tokenLoginUser(it) }
        val responseBody = loginUser?.let { UserDTO.UserResponseDTO(ulid = it.ulid, name = it.name, cellphone = it.cellphone, email = it.email, accessToken = tokens?.accessToken) }

        CookieUtil.addTokenCookies(tokens?.accessToken, tokens?.refreshToken, response)

        return ResponseEntity.ok(responseBody)

    }

    @PostMapping("/token/refresh")
    fun tokenRefresh(
        response: HttpServletResponse,
        @CookieValue(value = "accessToken") accessToken: String,
        @CookieValue(value = "refreshToken") refreshToken: String,
    ) : ResponseEntity<UserDTO.ValidRefreshTokenResponse>{

        val tokens = jwtTokenProvider.regenerateToken(accessToken, refreshToken)

        CookieUtil.addTokenCookies(tokens.accessToken, tokens.refreshToken, response)

        return ResponseEntity.ok(tokens)

    }

    @PostMapping("/auth")
    fun authLoginUser(@RequestBody @Valid requestBody: UserDTO.AuthLoginUserRequestDTO) : ResponseEntity<UserDTO.UserResponseDTO>{

        val authLoginUser = requestBody.cellphone.let { userService.authLogin(it) }
        val responseBody = authLoginUser?.let { UserDTO.UserResponseDTO(ulid= it.ulid, name = it.name, cellphone = it.cellphone, userId = it.userId) }

        return ResponseEntity.ok(responseBody)

    }

    @PostMapping("/auth/signup")
    fun authJoinUser(@RequestBody @Valid user: UserDTO.UserSMSRequestDTO) : ResponseEntity<SuccessResponse>{

        userService.authJoin(user) ?: throw RestApiException("인증 요청 중 오류가 발생하였습니다.")

        val responseBody = SuccessResponse("메시지 발송 성공")

        return ResponseEntity.ok(responseBody)

    }

    @PostMapping("/auth/confirm")
    fun confirmAuthLoginUser(@RequestBody @Valid saveObj : UserDTO.AuthConfirmRequestDTO, response: HttpServletResponse) : ResponseEntity<UserDTO.UserResponseDTO>{

        val confirmedUser = userService.authLoginConfirm(saveObj)

        val tokens = confirmedUser?.id?.let { userService.tokenLoginUser(it) }

        CookieUtil.addTokenCookies(tokens?.accessToken, tokens?.refreshToken, response)

        val responseBody = confirmedUser?.let { UserDTO.UserResponseDTO(ulid= it.ulid, name = it.name, cellphone = it.cellphone, userId = it.userId, accessToken = tokens?.accessToken) }

        return ResponseEntity.ok(responseBody)

    }


    @PostMapping("/auth/signup/confirm")
    fun confirmAuthJoinUser(@RequestBody @Valid saveObj : UserDTO.AuthConfirmRequestDTO) : ResponseEntity<UserDTO.AuthConfirmResponseDTO>{

        val smsAuthentication = userService.authConfirm(saveObj)
        val responseBody = smsAuthentication.id?.let { UserDTO.AuthConfirmResponseDTO(it) }

        return ResponseEntity.ok(responseBody)

    }

    @PostMapping("/signup")
    fun createUser(@RequestBody @Valid user: UserDTO.UserRequestDTO) : ResponseEntity<UserDTO.UserResponseDTO>{

        val signupUser = userService.register(user)
        val responseBody = signupUser?.let { UserDTO.UserResponseDTO(ulid= it.ulid, name = it.name, cellphone = it.cellphone) }

        return ResponseEntity.ok(responseBody)

    }

    @PatchMapping("/{ulid}")
    fun updateUser(@PathVariable("ulid") ulid: String, @RequestBody @Valid userRequest: UserDTO.UserRequestDTO) : ResponseEntity<UserDTO.UserResponseDTO>{

        val updatedUser = userService.updateUser(ulid, userRequest)

        val tokens = updatedUser?.let { userService.tokenLoginUser(it) }

        CookieUtil.addTokenCookies(tokens?.accessToken, tokens?.refreshToken, response)

        val responseBody = updatedUser?.let { UserDTO.UserResponseDTO(ulid= it.ulid, name = it.name, cellphone = it.cellphone, userId = it.userId) }

        return ResponseEntity.ok(responseBody)

    }


    @DeleteMapping("/{ulid}")
    fun deleteUser(@PathVariable("ulid") ulid: String): ResponseEntity<ApiResponse>{

        val deletedYn = userService.deleteUser(ulid)

        return if (deletedYn) {
            val successResponse = SuccessResponse("탈퇴 완료")
            ResponseEntity.ok(successResponse)
        } else {
            val errorCode = ErrorCode.BAD_REQUEST
            val errorResponse = ErrorResponse(errorCode.value, "탈퇴 실패")
            ResponseEntity.badRequest().body(errorResponse)
        }

    }

    @PostMapping("/logout")
    fun logout(
        response: HttpServletResponse,
        @RequestHeader("Authorization") authorization: String
    ) : ResponseEntity<ApiResponse>{

        val logoutYn = userService.logoutUser(authorization)

        return if (logoutYn) {
            val successResponse = SuccessResponse("로그아웃 완료")
            ResponseEntity.ok(successResponse)
        } else {
            val errorCode = ErrorCode.BAD_REQUEST
            val errorResponse = ErrorResponse(errorCode.value, "로그아웃 실패")
            ResponseEntity.badRequest().body(errorResponse)
        }


    }


}