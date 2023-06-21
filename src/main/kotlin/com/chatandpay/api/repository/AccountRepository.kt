package com.chatandpay.api.repository
import com.chatandpay.api.domain.OtherBankAccount
import com.chatandpay.api.domain.QOtherBankAccount
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
class AccountRepository (
    private val queryFactory: JPAQueryFactory
){

    @PersistenceContext
    private lateinit var entityManager: EntityManager

    fun save(account: OtherBankAccount): OtherBankAccount? {
        entityManager.persist(account)
        return account
    }


    fun findById(id: Long): OtherBankAccount? {
        val query = entityManager.createQuery("SELECT a FROM OtherBankAccount a WHERE a.id = :id", OtherBankAccount::class.java)
        query.setParameter("id", id)
        return query.resultList.lastOrNull()
    }


    fun findDuplicatedAccount(account: OtherBankAccount): Boolean {
        val query = entityManager.createQuery("SELECT a FROM OtherBankAccount a WHERE a.bankCode = :bankCode AND a.accountNumber = :accountNumber", OtherBankAccount::class.java)
        query.setParameter("bankCode", account.bankCode)
        query.setParameter("accountNumber", account.accountNumber)
        return query.resultList.isNotEmpty()
    }

    fun findByPayUserId(id: Long): List<OtherBankAccount>? {
        val query = entityManager.createQuery("SELECT a FROM OtherBankAccount a WHERE a.payUser.id = :id", OtherBankAccount::class.java)
        query.setParameter("id", id)
        return query.resultList
    }

    fun deleteAllAccount(userId: Long): Int{

        val qOtherBankAccount = QOtherBankAccount.otherBankAccount
        val deleteClause = queryFactory.delete(qOtherBankAccount)
            .where(qOtherBankAccount.payUser.id.eq(userId))

        val deletedCount = deleteClause.execute()

        entityManager.flush()
        entityManager.clear()

        return deletedCount.toInt()

    }


}