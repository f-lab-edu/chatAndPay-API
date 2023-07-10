package com.chatandpay.api.repository

import com.chatandpay.api.domain.Wallet
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.OptimisticLockException
import javax.persistence.PersistenceContext
@Repository
class WalletRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    fun save(wallet: Wallet): Wallet? {
        val maxRetries : Long = 5
        var retryCount = 0

        while (retryCount < maxRetries) {
            try {
                entityManager.merge(wallet)
                entityManager.flush()
                return wallet
            } catch (e: OptimisticLockException) {
                retryCount++
                Thread.sleep(1000 * maxRetries)
            }
        }

        throw OptimisticLockException("최대 재시도 횟수 $maxRetries 번을 초과하였습니다.")
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