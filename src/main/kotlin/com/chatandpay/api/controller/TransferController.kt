package com.chatandpay.api.controller

import com.chatandpay.api.dto.*
import com.chatandpay.api.service.TransferService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/transfers")
class TransferController (val transferService : TransferService) {


    @PostMapping("")
    fun sendTransfer(@RequestBody request: ReceiveTransferRequestDTO): ResponseEntity<ReceiveTransferResponseDTO> {

        val response = transferService.sendTransfer(request)
        return ResponseEntity.ok(response)

    }


    @PostMapping("/{transfer_id}")
    fun receiveTransfer(@PathVariable("transfer_id") id: UUID): ResponseEntity<SendTransferResponseDTO> {

        val response = transferService.receiveTransfer(id)
        return ResponseEntity.ok(response)

    }

    @PostMapping("/interbank")
    fun sendOtherBankTransfer(@RequestBody request: OtherBankTransferRequestDTO): ResponseEntity<OtherBankTransferResponseDTO> {

        val response = transferService.sendOtherBankTransfer(request)
        return ResponseEntity.ok(response)

    }

    @PostMapping("/registered")
    fun sendMyOtherBankTransfer(@RequestBody request: RegOtherBankTransferRequestDTO): ResponseEntity<OtherBankTransferResponseDTO> {

        val response = transferService.sendMyOtherBankTransfer(request)
        return ResponseEntity.ok(response)

    }



}