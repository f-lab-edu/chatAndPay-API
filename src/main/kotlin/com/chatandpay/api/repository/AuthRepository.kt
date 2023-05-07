package com.chatandpay.api.repository

import com.chatandpay.api.domain.sms.SmsAuthentication
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
class AuthRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    fun save(smsAuth: SmsAuthentication): SmsAuthentication? {
        return if (smsAuth.id == null) {
            entityManager.persist(smsAuth)
            smsAuth
        } else {
            entityManager.merge(smsAuth)
        }
    }

    fun findByCellphone(cellPhone: String) : SmsAuthentication? {
        val query = entityManager.createQuery("SELECT a FROM SmsAuthentication a WHERE a.phoneNumber = :phoneNumber", SmsAuthentication::class.java)
        query.setParameter("phoneNumber", cellPhone)
        return query.resultList.lastOrNull()
    }

}