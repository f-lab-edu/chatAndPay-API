package com.chatandpay.api.repository

import com.chatandpay.api.domain.Wallet
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
@Repository
class WalletRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    fun save(wallet: Wallet): Wallet? {
        entityManager.persist(wallet)
        return wallet
    }

    fun findByPayUserId(payUserId: Long): Wallet? {
        val query = entityManager.createQuery("SELECT w FROM Wallet w WHERE w.payUser.id = :payUserId", Wallet::class.java)
        query.setParameter("payUserId", payUserId)
        return query.resultList.firstOrNull()
    }

    fun delete(wallet: Wallet) : Boolean {
        return try {
            entityManager.remove(wallet)
            true
        } catch (e: Exception) {
            false
        }
    }

}