package com.chatandpay.api.controller

import com.chatandpay.api.code.ErrorCode
import com.chatandpay.api.common.ApiResponse
import com.chatandpay.api.common.ErrorResponse
import com.chatandpay.api.common.SuccessResponse
import com.chatandpay.api.dto.PayUserDTO
import com.chatandpay.api.dto.SignUpPayUserDTO
import com.chatandpay.api.service.PayUserService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/pay-users")
class PayUserController(val payUserService: PayUserService)  {

    @PostMapping("/signup")
    fun signUpPayUser(@RequestBody payUser: SignUpPayUserDTO): ResponseEntity<PayUserDTO> {

        val response = payUserService.register(payUser) ?: throw RuntimeException("페이 회원 가입 실패")
        return ResponseEntity.ok(response)

    }


    @DeleteMapping("/{id}")
    fun withdrawPayService(@PathVariable("id") id: Long): ResponseEntity<ApiResponse> {

        val deletedYn = payUserService.withdrawPayService(id)

        return if (deletedYn) {
            val successResponse = SuccessResponse("탈퇴 완료")
            ResponseEntity.ok(successResponse)
        } else {
            val errorCode = ErrorCode.INTERNAL_SERVER_ERROR
            val errorResponse = ErrorResponse(errorCode.value, "탈퇴 실패")
            ResponseEntity.badRequest().body(errorResponse)
        }

    }
}