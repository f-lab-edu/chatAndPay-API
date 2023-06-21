package com.chatandpay.api.repository

import com.chatandpay.api.domain.TransferChangeLog
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface TransferChangeLogRepository : MongoRepository<TransferChangeLog, String>