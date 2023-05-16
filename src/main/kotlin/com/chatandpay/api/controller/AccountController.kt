package com.chatandpay.api.controller

import com.chatandpay.api.dto.OtherBankAccountRequestDTO
import com.chatandpay.api.service.AccountService
import com.chatandpay.api.dto.DepositWalletDTO
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


    @PostMapping("/deposit")
    fun chargeWalletformAccount(@RequestBody tempMap: Map<String, String>): ResponseEntity<Any> {

        val accountId = (tempMap["accountId"])?.toLong()!!
        val withdrawalAmount = (tempMap["withdrawalAmount"])?.toInt()!!

        val tempDepositWallet = DepositWalletDTO(withdrawalAmount, accountId)
        // TODO DTO 위치 확정 필요

        try {
            accountService.chargeWallet(tempDepositWallet)
        } catch(e: Exception) {
            return ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity.ok("ok")
    }


}