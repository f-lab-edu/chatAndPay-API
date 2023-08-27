package com.chatandpay.api.controller

import com.chatandpay.api.component.RedissonLockFacade
import com.chatandpay.api.dto.*
import com.chatandpay.api.service.TransferService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/transfers")
class TransferController (
    val transferService : TransferService,
    val redissonLockFacade: RedissonLockFacade
) {


    @PostMapping("")
    fun sendTransfer(@RequestBody @Valid request: TransferDTO.ReceiveTransferRequestDTO): ResponseEntity<TransferDTO.ReceiveTransferResponseDTO> {

        val response = redissonLockFacade.sendTransfer(request)
        return ResponseEntity.ok(response)

    }


    @PostMapping("/{transfer_id}")
    fun receiveTransfer(@PathVariable("transfer_id") id: UUID): ResponseEntity<TransferDTO.SendTransferResponseDTO> {

        val response = redissonLockFacade.receiveTransfer(id)
        return ResponseEntity.ok(response)

    }

    @PostMapping("/interbank")
    fun sendOtherBankTransfer(@RequestBody @Valid request: TransferDTO.OtherBankTransferRequestDTO): ResponseEntity<TransferDTO.OtherBankTransferResponseDTO> {

        val response = transferService.sendOtherBankTransfer(request)
        return ResponseEntity.ok(response)

    }

    @PostMapping("/registered")
    fun sendMyOtherBankTransfer(@RequestBody @Valid request: TransferDTO.RegOtherBankTransferRequestDTO): ResponseEntity<TransferDTO.OtherBankTransferResponseDTO> {

        val response = transferService.sendMyOtherBankTransfer(request)
        return ResponseEntity.ok(response)

    }

    @GetMapping("/pending/{sender}")
    fun getPendingTransfers(@PathVariable("sender") ulid: String) : ResponseEntity<List<TransferDTO.PendingTransferDTO>> {

        val response = transferService.getPendingTransfers(ulid)
        return ResponseEntity.ok(response)

    }


    @PostMapping("/change")
    fun changePendingTransferState(@RequestBody @Valid request: TransferDTO.ChangePendingTransferRequestDTO) : ResponseEntity<TransferDTO.ChangePendingTransferResponseDTO> {

        val response = transferService.changePendingTransferState(request)
        return ResponseEntity.ok(response)

    }


}