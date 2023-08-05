package com.chatandpay.api.controller

import com.chatandpay.api.component.AccountCheck
import com.chatandpay.api.dto.AccountDTO
import com.chatandpay.api.dto.*
import com.chatandpay.api.service.AccountService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/accounts")
class AccountController(
    val accountCheck : AccountCheck,
    val accountService: AccountService,
) {

    @GetMapping("")
    fun getExternalAccount(@RequestParam userId: Long) : ResponseEntity<AccountDTO.OtherBankAccountListResponseDTO> {
        // TODO RequestParam - Token 확인으로 변경
        val response = accountService.getExternalAccount(userId)
        return ResponseEntity.ok(response)
    }

    @PostMapping("")
    fun addExternalAccount(@RequestBody @Valid account: AccountDTO.OtherBankAccountRequestDTO): ResponseEntity<AccountDTO.OtherBankAccountResponseDTO> {

        val response = accountCheck.saveAccount(account)
        return ResponseEntity.ok(response)

    }


    @PostMapping("/deposit")
    fun chargeWalletFromAccount(@RequestBody @Valid depositMoney: AccountDTO.DepositWalletRequestDTO): ResponseEntity<AccountDTO.DepositWalletResponseDTO> {

        val response = accountCheck.chargeWallet(depositMoney)
        return ResponseEntity.ok(response)

    }


}