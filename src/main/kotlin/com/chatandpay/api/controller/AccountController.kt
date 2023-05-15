package com.chatandpay.api.controller

import com.chatandpay.api.controller.dto.OtherBankAccountRequestDTO
import com.chatandpay.api.service.AccountService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/accounts")
class AccountController(val accountService : AccountService) {

    @PostMapping("")
    fun addExternalAccount(@RequestBody account: OtherBankAccountRequestDTO): ResponseEntity<Any> {

        try {
            accountService.saveAccount(account)
        } catch(e: Exception) {
            return ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity.ok("ok")
    }




}