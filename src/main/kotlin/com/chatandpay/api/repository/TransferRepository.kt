package com.chatandpay.api.repository

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

    fun findById(id: UUID) : Transfer? {
        return entityManager.find(Transfer::class.java, id)
    }

}
