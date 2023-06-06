package com.chatandpay.api.service

import com.chatandpay.api.domain.Log
import com.chatandpay.api.repository.LogRepository
import org.springframework.stereotype.Component

@Component
class LogService(private val logRepository: LogRepository) {

    fun saveLog(log: Log) {
        logRepository.save(log)
    }

}