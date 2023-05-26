package com.chatandpay.api.controller

import com.chatandpay.api.code.ErrorCode
import com.chatandpay.api.common.ApiResponse
import com.chatandpay.api.common.ErrorResponse
import com.chatandpay.api.common.SuccessResponse
import com.chatandpay.api.domain.User
import com.chatandpay.api.dto.UserDTO
import com.chatandpay.api.dto.AuthConfirmResponseDTO
import com.chatandpay.api.dto.AuthConfirmRequestDTO
import com.chatandpay.api.service.UserService
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/users")
class UserController(val userService: UserService) {

    @PostMapping("/login")
    fun loginUser(@RequestBody user: User) : ResponseEntity<UserDTO>{

        val loginUser = userService.login(user)
        val responseBody = loginUser?.let { UserDTO(it.name, loginUser.cellphone, loginUser.userId, null) }

        return ResponseEntity.ok(responseBody)

    }

    @PostMapping("/auth")
    fun authLoginUser(@RequestBody requestBody: String) : ResponseEntity<UserDTO>{

        val objectMapper = ObjectMapper()
        val jsonObject = objectMapper.readValue(requestBody, Map::class.java)
        val cellphone = jsonObject["cellphone"].toString()

        val authLoginUser = userService.authLogin(cellphone)
        val responseBody = authLoginUser?.let { UserDTO(it.name, it.cellphone, it.userId,null) }

        return ResponseEntity.ok(responseBody)

    }

    @PostMapping("/auth/signup")
    fun authJoinUser(@RequestBody user: User) : ResponseEntity<SuccessResponse>{

        userService.authJoin(user) ?: throw RuntimeException("인증 요청 중 오류가 발생하였습니다.")

        val responseBody = SuccessResponse("메시지 발송 성공")

        return ResponseEntity.ok(responseBody)

    }

    @PostMapping("/auth/confirm")
    fun confirmAuthLoginUser(@RequestBody saveObj : AuthConfirmRequestDTO) : ResponseEntity<UserDTO>{

        val confirmedUser = userService.authLoginConfirm(saveObj)

        val responseBody = confirmedUser?.let { UserDTO(it.name, it.cellphone, it.userId, null) }

        return ResponseEntity.ok(responseBody)

    }


    @PostMapping("/auth/signup/confirm")
    fun confirmAuthJoinUser(@RequestBody saveObj : AuthConfirmRequestDTO) : ResponseEntity<AuthConfirmResponseDTO>{

        val smsAuthentication = userService.authConfirm(saveObj)

        val responseBody = smsAuthentication.id?.let { AuthConfirmResponseDTO(it) }

        return ResponseEntity.ok(responseBody)

    }

    @PostMapping("/signup")
    fun createUser(@RequestBody user: User) : ResponseEntity<UserDTO>{

        val signupUser = userService.register(user)
        val responseBody = signupUser?.let { UserDTO(it.name, it.cellphone, it.userId, null) }

        return ResponseEntity.ok(responseBody)

    }

    @PatchMapping("/{id}")
    fun updateUser(@PathVariable("id") id: Long, @RequestBody userRequest: User) : ResponseEntity<UserDTO>{

        val updatedUser = userService.updateUser(id, userRequest)
        val responseBody = updatedUser?.let { UserDTO(it.name, it.cellphone, it.userId, null) }

        return ResponseEntity.ok(responseBody)

    }


    @DeleteMapping("/{id}")
    fun updateUser(@PathVariable("id") id: Long): ResponseEntity<ApiResponse>{

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