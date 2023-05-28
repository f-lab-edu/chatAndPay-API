package com.chatandpay.api.repository

import com.chatandpay.api.domain.AccountApiLog
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
class ApiResultLogRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    fun saveAccountApiLog(account: AccountApiLog): AccountApiLog? {
        entityManager.persist(account)
        return account
    }


}