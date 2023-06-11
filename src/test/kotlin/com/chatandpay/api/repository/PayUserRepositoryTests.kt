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
class PayUserRepositoryTests {

    @Autowired
    private lateinit var payUserRepository: PayUserRepository

    @PersistenceContext
    private lateinit var entityManager: EntityManager


    @Test
    @Transactional
    fun save_ValidUser() {

        // given
        val user = User(null, "01012121212", "", "", "이름", 1)
        val payUser = PayUser(null, "testCi", user, "testUserSeqNo", "20230501", null)
        val wallet = Wallet(null, 100, payUser)
        payUser.wallet = wallet

        entityManager.persist(user)
        entityManager.persist(wallet)

        // when
        val savedUser = payUserRepository.save(payUser)

        // then
        assertNotNull(savedUser!!.id)
        assertEquals(payUser, savedUser)

    }


    @Test
    @Transactional
    fun findById_ValidUser() {

        // given
        val user = User(null, "01012121212", "", "", "이름", 1)
        val payUser = PayUser(null, "testCi", user, "testUserSeqNo", "20230501", null)
        val wallet = Wallet(null, 100, payUser)
        payUser.wallet = wallet

        entityManager.persist(user)
        entityManager.persist(wallet)
        entityManager.persist(payUser)

        // when
        val foundUser = payUserRepository.findById(payUser.id!!)

        // then
        assertNotNull(foundUser)
        assertEquals("testCi", foundUser?.ci)
        assertEquals("testUserSeqNo", foundUser?.userSeqNo)

    }


    @Test
    @Transactional
    fun findByCi_ValidUser() {

        // given
        val user = User(null, "01012121212", "", "", "이름", 1)
        val payUser = PayUser(null, "testCi", user, "testUserSeqNo", "20230501", null)
        val wallet = Wallet(null, 100, payUser)
        payUser.wallet = wallet

        entityManager.persist(user)
        entityManager.persist(wallet)
        entityManager.persist(payUser)

        // when
        val foundUser = payUserRepository.findByCi(payUser.ci)

        // then
        assertNotNull(foundUser)
        assertEquals("testCi", foundUser?.ci)
        assertEquals("testUserSeqNo", foundUser?.userSeqNo)

    }


    @Test
    @Transactional
    fun findByUserId_ValidUser() {

        // given
        val user = User(null, "01012121212", "", "", "이름", 1)
        val payUser = PayUser(null, "testCi", user, "testUserSeqNo", "20230501", null)
        val wallet = Wallet(null, 100, payUser)
        payUser.wallet = wallet

        entityManager.persist(user)
        entityManager.persist(wallet)
        entityManager.persist(payUser)

        // when
        val foundUser = payUserRepository.findByUserId(user.id!!)

        // then
        assertNotNull(foundUser)
        assertEquals("testCi", foundUser?.ci)
        assertEquals("testUserSeqNo", foundUser?.userSeqNo)

    }

    @Test
    @Transactional
    fun delete_ValidUser() {

        // given
        val user = User(null, "01012121212", "", "", "이름", 1)
        val payUser = PayUser(null, "testCi", user, "testUserSeqNo", "20230501", null)
        val wallet = Wallet(null, 100, payUser)
        payUser.wallet = wallet

        entityManager.persist(user)
        entityManager.persist(wallet)
        entityManager.persist(payUser)

        // when
        val deleted = payUserRepository.delete(payUser)

        // then
        assertTrue(deleted)
        assertNull(entityManager.find(PayUser::class.java, payUser.id))

    }


}