package com.chatandpay.api.repository

import com.chatandpay.api.domain.OtherBankAccount
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
class AccountRepository {

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    fun save(account: OtherBankAccount): OtherBankAccount? {
        entityManager.persist(account)
        return account
    }

    fun findDuplicatedAccount(account: OtherBankAccount): Boolean {
        val query = entityManager.createQuery("SELECT a FROM OtherBankAccount a WHERE a.bankCode = :bankCode AND a.accountNumber = :accountNumber", OtherBankAccount::class.java)
        query.setParameter("bankCode", account.bankCode)
        query.setParameter("accountNumber", account.accountNumber)
        return query.resultList.isNotEmpty()
    }


}