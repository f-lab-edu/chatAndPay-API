package com.chatandpay.api.controller

import com.chatandpay.api.common.ApiResponse
import com.chatandpay.api.common.ErrorResponse
import com.chatandpay.api.common.SuccessResponse
import com.chatandpay.api.domain.User
import com.chatandpay.api.dto.UserDTO
import com.chatandpay.api.service.UserService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ObjectNode
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/users")
class UserController(val userService: UserService) {

    @PostMapping("/login")
    fun loginUser(@RequestBody user: User) : ResponseEntity<UserDTO>{

        val loginUser = userService.login(user)
        val responseBody = loginUser?.let { UserDTO(it.name, loginUser.cellphone, loginUser.userId) }

        return ResponseEntity.ok(responseBody)

    }

    @PostMapping("/auth")
    fun authLoginUser(@RequestBody user: User) : ResponseEntity<UserDTO>{

        val authLoginUser = userService.authLogin(user)
        val responseBody = authLoginUser?.let { UserDTO(it.name, it.cellphone, it.userId) }

        return ResponseEntity.ok(responseBody)

    }

    @PostMapping("/authJoin")
    fun authJoinUser(@RequestBody user: User) : ResponseEntity<UserDTO>{

        val authJoinUser = userService.authLogin(user)
        val responseBody = authJoinUser?.let { UserDTO(it.name, it.cellphone, it.userId) }

        return ResponseEntity.ok(responseBody)

    }

    @PostMapping("/authConfirm")
    fun confirmAuthLoginUser(@RequestBody saveObj : ObjectNode) : ResponseEntity<UserDTO>{

        val mapper = ObjectMapper()
        val authNumber = mapper.treeToValue(saveObj["authNumber"], String::class.java)
        val user = mapper.treeToValue(saveObj["user"], User::class.java)

        val confirmedUser = userService.authLoginConfirm(user, authNumber)
        val responseBody = confirmedUser?.let { UserDTO(it.name, it.cellphone, it.userId) }

        return ResponseEntity.ok(responseBody)

    }

    @PostMapping("/signup")
    fun createUser(@RequestBody user: User) : ResponseEntity<UserDTO>{

        val signupUser = userService.register(user)
        val responseBody = signupUser?.let { UserDTO(it.name, it.cellphone, it.userId) }

        return ResponseEntity.ok(responseBody)

    }

    @PatchMapping("/{id}")
    fun updateUser(@PathVariable("id") id: Long, @RequestBody userRequest: User) : ResponseEntity<UserDTO>{

        val updatedUser = userService.updateUser(id, userRequest)
        val responseBody = updatedUser?.let { UserDTO(it.name, it.cellphone, it.userId) }

        return ResponseEntity.ok(responseBody)

    }


    @DeleteMapping("/{id}")
    fun updateUser(@PathVariable("id") id: Long): ResponseEntity<ApiResponse>{

        val deletedYn = userService.deleteUser(id)

        return if (deletedYn) {
            val successResponse = SuccessResponse(HttpStatus.OK, "탈퇴 완료")
            ResponseEntity.ok(successResponse)
        } else {
            val errorResponse = ErrorResponse(HttpStatus.BAD_REQUEST, "탈퇴 실패")
            ResponseEntity.badRequest().body(errorResponse)
        }

    }

}