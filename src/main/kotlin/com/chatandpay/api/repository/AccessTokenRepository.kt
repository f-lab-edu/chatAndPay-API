package com.chatandpay.api.repository

import com.chatandpay.api.domain.AccessToken
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
class AccessTokenRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    fun save(token: AccessToken): AccessToken? {
        entityManager.persist(token)
        return token
    }

    fun findLatestAliveToken(expiredDate : LocalDateTime) : AccessToken? {
        val query = entityManager.createQuery("SELECT a FROM AccessToken a WHERE a.createdAt > :dateTime", AccessToken::class.java)
        query.setParameter("dateTime", expiredDate)
        return query.resultList.firstOrNull()
    }

}