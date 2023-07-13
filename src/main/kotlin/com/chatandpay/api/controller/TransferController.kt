package com.chatandpay.api.controller

import com.chatandpay.api.component.RedissonLockFacade
import com.chatandpay.api.dto.*
import com.chatandpay.api.service.TransferService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/transfers")
class TransferController (
    val transferService : TransferService,
    val redissonLockFacade: RedissonLockFacade
) {


    @PostMapping("")
    fun sendTransfer(@RequestBody request: ReceiveTransferRequestDTO): ResponseEntity<ReceiveTransferResponseDTO> {

        val response = redissonLockFacade.sendTransfer(request)
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

    @GetMapping("/pending/{sender}")
    fun getPendingTransfers(@PathVariable("sender") id: Long) : ResponseEntity<List<PendingTransferDTO>> {

        // TODO 로그인 유지 (세션 등) 구현 후 sender부 변경
        val response = transferService.getPendingTransfers(id)
        return ResponseEntity.ok(response)

    }


    @PostMapping("/change")
    fun changePendingTransferState(@RequestBody request: ChangePendingTransferRequestDTO) : ResponseEntity<ChangePendingTransferResponseDTO> {

        val response = transferService.changePendingTransferState(request)
        return ResponseEntity.ok(response)

    }


}