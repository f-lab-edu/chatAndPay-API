package com.chatandpay.api

import com.chatandpay.api.common.UserRole
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
        val user = User(name = "이름", userId= "", password= "", cellphone = "01012345678", role= UserRole.USER, verificationId = 1)

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
        val user = User(name = "이름", userId= "", password= "", cellphone = "01012345678", role= UserRole.USER, verificationId = 1)
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
        val user = User(name = "이름", userId= "", password= "", cellphone = "01012345678", role= UserRole.USER, verificationId = 1)
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
        val user = User(name = "", userId= "testId", password= "", cellphone = "", role= UserRole.USER, verificationId = 1)
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
        val tryUser = User(id = 1, name = "시도유저", userId= "", password= "", cellphone = "01034453445", role= UserRole.USER, verificationId = 1)
        val existUser = User(null, name = "기존유저", userId= "", password= "", cellphone = "01034453445", role= UserRole.USER, verificationId = 1)

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
        val tryUser = User(id = 1, name = "시도유저", userId= "testId", password= "", cellphone = "01012121212", role= UserRole.USER, verificationId = 1)
        val existUser = User(null, name = "기존유저", userId= "testId", password= "", cellphone = "01034453445", role= UserRole.USER, verificationId = 1)

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
        val user = User(name = "이름", userId= "", password= "", cellphone = "01012345678", role= UserRole.USER, verificationId = 1)
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
        val user = User(name = "이름", userId= "", password= "", cellphone = "01012345678", role= UserRole.USER, verificationId = 1)
        entityManager.persist(user)

        // when
        val deleted = userRepository.delete(user)

        // then
        assertTrue(deleted)
        assertNull(entityManager.find(User::class.java, user.id))

    }


}