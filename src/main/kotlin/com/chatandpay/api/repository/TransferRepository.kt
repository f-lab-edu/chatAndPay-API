package com.chatandpay.api.repository

import com.chatandpay.api.domain.OtherBankTransfer
import com.chatandpay.api.domain.PayUser
import com.chatandpay.api.domain.Transfer
import org.springframework.stereotype.Repository
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
class TransferRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    fun save(transfer: Transfer): Transfer? {
        entityManager.persist(transfer)
        return transfer
    }

    fun save(transfer: OtherBankTransfer): OtherBankTransfer? {
        entityManager.persist(transfer)
        return transfer
    }

    fun findById(id: UUID) : Transfer? {
        return entityManager.find(Transfer::class.java, id)
    }

    fun findUnsentTransfer(payUser: PayUser): List<Transfer> {
        val query = entityManager.createQuery("SELECT t FROM Transfer t WHERE t.sender = :payUser AND t.transferred = false", Transfer::class.java)
        query.setParameter("payUser", payUser)
        return query.resultList
    }

    fun findUnreceivedTransfer(payUser: PayUser): List<Transfer> {
        val query = entityManager.createQuery("SELECT t FROM Transfer t WHERE t.receiver = :payUser AND t.transferred = false", Transfer::class.java)
        query.setParameter("payUser", payUser)
        return query.resultList
    }

}
