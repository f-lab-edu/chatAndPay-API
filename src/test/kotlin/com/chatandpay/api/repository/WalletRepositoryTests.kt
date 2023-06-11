package com.chatandpay.api.repository

import com.chatandpay.api.domain.PayUser
import com.chatandpay.api.domain.User
import com.chatandpay.api.domain.Wallet
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@SpringBootTest
class WalletRepositoryTests {

    @Autowired
    private lateinit var walletRepository: WalletRepository

    @PersistenceContext
    private lateinit var entityManager: EntityManager


    @Test
    @Transactional
    fun save_ValidWallet() {

        // given
        val user = User(null, "01012121212", "", "", "이름", 1)
        val payUser = PayUser(null, "testCi", user, "testUserSeqNo", "20230501", null)
        val wallet = Wallet(null, 100, payUser)
        payUser.wallet = wallet

        entityManager.persist(user)
        entityManager.persist(payUser)

        // when
        val savedWallet = walletRepository.save(wallet)

        // then
        assertNotNull(savedWallet!!.id)
        assertEquals(wallet.payUser, payUser)

    }


    @Test
    @Transactional
    fun findByPayUserId_ValidWallet() {

        // given
        val user = User(null, "01012121212", "", "", "이름", 1)
        val payUser = PayUser(null, "testCi", user, "testUserSeqNo", "20230501", null)
        val wallet = Wallet(null, 100, payUser)
        payUser.wallet = wallet

        entityManager.persist(user)
        entityManager.persist(wallet)
        entityManager.persist(payUser)

        // when
        val foundWallet = walletRepository.findByPayUserId(payUser.id!!)

        // then
        assertNotNull(foundWallet!!.id)
        assertEquals(wallet, foundWallet)

    }

    @Test
    @Transactional
    fun delete_ValidWallet() {

        // given
        val user = User(null, "01012121212", "", "", "이름", 1)
        val payUser = PayUser(null, "testCi", user, "testUserSeqNo", "20230501", null)
        val wallet = Wallet(null, 100, payUser)
        payUser.wallet = wallet

        entityManager.persist(user)
        entityManager.persist(wallet)
        entityManager.persist(payUser)

        // when
        val deleted = walletRepository.delete(wallet)

        // then
        assertTrue(deleted)
        assertNull(entityManager.find(Wallet::class.java, wallet.id))

    }


}