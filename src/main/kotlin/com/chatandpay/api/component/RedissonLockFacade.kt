package com.chatandpay.api.component

import com.chatandpay.api.dto.TransferDTO
import com.chatandpay.api.exception.RestApiException
import com.chatandpay.api.service.TransferService
import org.redisson.api.RLock
import org.redisson.api.RedissonClient
import org.springframework.stereotype.Component
import java.util.*
import java.util.concurrent.TimeUnit

@Component
class RedissonLockFacade (
    private val redissonClient: RedissonClient,
    private val transferService: TransferService
){

    fun sendTransfer(request: TransferDTO.ReceiveTransferRequestDTO) : TransferDTO.ReceiveTransferResponseDTO? {

        val uuid = UUID.randomUUID()
        val lock: RLock = redissonClient.getLock("transfer")

        try {
            val available = lock.tryLock(100, 1, TimeUnit.SECONDS)
            if (!available) {
                throw RestApiException("lock 획득 실패")
            }
            return transferService.sendTransfer(request, uuid)
        } catch (e: Exception) {
            throw RestApiException(e.message)
        } finally {
            lock.unlock()
        }
    }


}