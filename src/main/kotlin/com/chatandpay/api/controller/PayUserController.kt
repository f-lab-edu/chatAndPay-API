package com.chatandpay.api.controller

import com.chatandpay.api.controller.dto.SignUpPayUserDTO
import com.chatandpay.api.service.PayUserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/pay-users")
class PayUserController(val payUserService: PayUserService)  {

    @PostMapping("/signup")
    fun signUpPayUser(@RequestBody payUser: SignUpPayUserDTO): ResponseEntity<Any> {

        try {
            payUserService.register(payUser)
        } catch(e: Exception) {
            return ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity.ok("ok")
    }


    @DeleteMapping("/{id}")
    fun withdrawPayService(@PathVariable("id") id: Long): ResponseEntity<Any> {

        try {
            payUserService.withdrawPayService(id)
        } catch(e: Exception) {
            return ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity.ok("ok")
    }
}