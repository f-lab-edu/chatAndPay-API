package com.chatandpay.api.repository

import com.chatandpay.api.domain.Wallet
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import java.util.*
import javax.jdo.annotations.Transactional
import javax.persistence.*

@Repository
class WalletRepository {

    private val logger: Logger = LoggerFactory.getLogger(WalletRepository::class.java)

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    @Transactional
    fun save(wallet: Wallet, uuid: UUID): Wallet? {
        val maxRetries = 5
        var retryCount = 0
        var updatedCount = 0

        while (retryCount < maxRetries) {
            val managedEntity = entityManager.find(Wallet::class.java, wallet.id)

            if (managedEntity != null) {

                logger.info("managedEntity!!!! ${managedEntity.version}")

                if (managedEntity.version != wallet.version) {
                    retryCount++
                    Thread.sleep(1000)
                    continue
                }

                val updateQuery = entityManager.createQuery("UPDATE Wallet w SET w.money = :money, w.version = :version WHERE w.id = :id")
                updateQuery.setParameter("money", wallet.money)
                updateQuery.setParameter("version", wallet.version?.plus(1))
                updateQuery.setParameter("id", wallet.id)

                updatedCount = updateQuery.executeUpdate()

                entityManager.flush()
                break
            }
        }

        if (retryCount >= maxRetries) {
            throw OptimisticLockException("재시도 횟수 $maxRetries 번을 초과하였습니다.")
        }

        if (updatedCount > 0) {
            entityManager.refresh(wallet)
            logger.info("refresh!!!!! ${wallet.version}")
        }

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