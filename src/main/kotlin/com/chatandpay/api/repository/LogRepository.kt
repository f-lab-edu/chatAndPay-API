package com.chatandpay.api.repository

import com.chatandpay.api.domain.Log
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface LogRepository : MongoRepository<Log, String>