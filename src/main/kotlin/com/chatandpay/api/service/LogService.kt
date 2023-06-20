package com.chatandpay.api.service

import com.chatandpay.api.domain.Log
import com.chatandpay.api.domain.TransferChangeLog
import com.chatandpay.api.repository.LogRepository
import com.chatandpay.api.repository.TransferChangeLogRepository
import org.springframework.stereotype.Component

@Component
class LogService(
    private val logRepository: LogRepository,
    private val transferChangeLogRepository : TransferChangeLogRepository
) {

    fun saveLog(log: Log) {
        logRepository.save(log)
    }

    fun saveTransferChangeLog(log: TransferChangeLog) {
        transferChangeLogRepository.save(log)
    }

}