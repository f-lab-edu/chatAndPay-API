package com.chatandpay.api.controller

import com.chatandpay.api.component.AccountCheck
import com.chatandpay.api.dto.AccountDTO
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid


@RestController
@RequestMapping("/accounts")
class AccountController(
    val accountCheck : AccountCheck,
) {

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