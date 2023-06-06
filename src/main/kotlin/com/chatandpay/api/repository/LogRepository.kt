package com.chatandpay.api.repository

import com.chatandpay.api.domain.Log
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Component


@Component
class LogRepository(private val mongoTemplate: MongoTemplate) {

    fun saveLog(log: Log) {
        mongoTemplate.save(log)
    }

}