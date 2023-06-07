package com.chatandpay.api

import com.chatandpay.api.domain.User
import com.chatandpay.api.repository.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@SpringBootTest
class UserRepositoryTests {

    @Autowired
    private lateinit var userRepository: UserRepository

    @PersistenceContext
    private lateinit var entityManager: EntityManager


    @Test
    @Transactional
    fun save_ValidUser() {

        // given
        val user = User(null, "01012345678", null, null, "이름", 1)

        // when
        val savedUser = userRepository.save(user)

        // then
        assertNotNull(savedUser!!.id)
        assertEquals(user, savedUser)

    }

    @Test
    @Transactional
    fun findByName_ValidUser() {
        // given
        val user =  User(null, "01012345678", null, null, "이름", 1)
        entityManager.persist(user)

        // when
        val foundUser = userRepository.findByName("이름")

        // then
        assertNotNull(foundUser)
        assertEquals("이름", foundUser?.name)
    }

    @Test
    @Transactional
    fun findByCellphone_ValidUser() {
        // given
        val user =  User(null, "01012345678", null, null, "이름", 1)
        entityManager.persist(user)

        // when
        val foundUser = userRepository.findByCellphone("01012345678")

        // then
        assertNotNull(foundUser)
        assertEquals("01012345678", foundUser?.cellphone)
    }

    @Test
    @Transactional
    fun findByUserId_ValidUser() {

        // given
        val user =  User(null, "", "testId", null, "", -1)
        entityManager.persist(user)

        // when
        val foundUser = userRepository.findByUserId("testId")

        // then
        assertNotNull(foundUser)
        assertEquals("testId", foundUser?.userId)

    }

    @Test
    @Transactional
    fun existsByCellphoneAndIdNot_ValidUser() {

        // given
        val tryUser = User(1, "01012341234", "testId", null, "시도유저", -1)
        val existUser = User(null, "01012341234", null, null, "기존유저", 1)
        entityManager.persist(existUser)

        // when
        val exists = userRepository.existsByCellphoneAndIdNot(tryUser.cellphone, tryUser.id!!)

        // then
        assertTrue(exists)

    }

    @Test
    @Transactional
    fun existsByUserIdAndIdNot_ValidUser() {

        // given
        val tryUser = User(1, "01012121212", "testId", null, "시도유저", -1)
        val existUser = User(null, "01012341234", "testId", null, "기존유저", 1)
        entityManager.persist(existUser)


        // when
        val exists = userRepository.existsByUserIdAndIdNot(tryUser.userId!!, tryUser.id!!)

        // then
        assertTrue(exists)

    }



    @Test
    @Transactional
    fun findById_ValidUser() {

        // given
        val user = User(null, "01012345678", null, null, "이름", 1)
        entityManager.persist(user)

        // when
        val foundUser = userRepository.findById(user.id!!)

        // then
        assertNotNull(foundUser)
        assertEquals(user.id, foundUser?.id)
        assertEquals("이름", foundUser?.name)

    }

    @Test
    @Transactional
    fun delete_ValidUser() {

        // given
        val user = User(null, "01012345678", null, null, "이름", 1)
        entityManager.persist(user)

        // when
        val deleted = userRepository.delete(user)

        // then
        assertTrue(deleted)
        assertNull(entityManager.find(User::class.java, user.id))

    }


}