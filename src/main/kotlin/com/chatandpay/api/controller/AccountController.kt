package com.chatandpay.api.controller

import com.chatandpay.api.component.AccountCheck
import com.chatandpay.api.dto.OtherBankAccountRequestDTO
import com.chatandpay.api.dto.DepositWalletRequestDTO
import com.chatandpay.api.dto.OtherBankAccountResponseDTO
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
    fun addExternalAccount(@RequestBody account: OtherBankAccountRequestDTO): ResponseEntity<OtherBankAccountResponseDTO> {

        val response = accountCheck.saveAccount(account)
        return ResponseEntity.ok(response)

    }


    @PostMapping("/deposit")
    fun chargeWalletfromAccount(@RequestBody depositMoney: DepositWalletRequestDTO): ResponseEntity<Any> {

        val response = accountCheck.chargeWallet(depositMoney)
        return ResponseEntity.ok(response)

    }


}