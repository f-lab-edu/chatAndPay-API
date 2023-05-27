package com.chatandpay.api.controller

import com.chatandpay.api.component.AccountCheck
import com.chatandpay.api.dto.OtherBankAccountRequestDTO
import com.chatandpay.api.dto.DepositWalletDTO
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/accounts")
class AccountController(
    val accountCheck : AccountCheck,
) {

    @PostMapping("")
    fun addExternalAccount(@RequestBody account: OtherBankAccountRequestDTO): ResponseEntity<Any> {

        try {
            accountCheck.saveAccount(account)
        } catch(e: Exception) {
            return ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity.ok("ok")
    }


    @PostMapping("/deposit")
    fun chargeWalletfromAccount(@RequestBody depositMoney: DepositWalletDTO): ResponseEntity<Any> {

        try {
            accountCheck.chargeWallet(depositMoney)
        } catch(e: Exception) {
            return ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity.ok("ok")
    }


}