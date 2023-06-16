package com.chatandpay.api.repository

import com.chatandpay.api.domain.PayUser
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
class PayUserRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    fun save(user: PayUser): PayUser? {
        entityManager.persist(user)
        return user
    }

    fun findById(id: Long): PayUser? {
        return entityManager.find(PayUser::class.java, id)
    }

    fun findByCi(ci: String): PayUser? {
        val query = entityManager.createQuery("SELECT u FROM PayUser u WHERE u.ci = :ci", PayUser::class.java)
        query.setParameter("ci", ci)
        return query.resultList.firstOrNull()
    }

    fun findByUserId(userId: Long): PayUser? {
        val query = entityManager.createQuery("SELECT u FROM PayUser u WHERE u.user.id = :userId", PayUser::class.java)
        query.setParameter("userId", userId)
        return query.resultList.firstOrNull()
    }

    fun delete(user: PayUser) : Boolean {
         return try {
             entityManager.persist(user)
             true
        } catch (e: Exception) {
             false
        }
    }

}
