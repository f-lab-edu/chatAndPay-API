package com.chatandpay.api

import com.chatandpay.api.domain.User
import com.chatandpay.api.domain.sms.SmsAuthentication
import com.chatandpay.api.repository.AuthRepository
import com.chatandpay.api.repository.UserRepository
import com.chatandpay.api.service.UserService
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.security.crypto.password.PasswordEncoder
import javax.persistence.EntityNotFoundException

@SpringBootTest
class UserServiceTests {

    @MockBean
    private lateinit var authRepository: AuthRepository

    @MockBean
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var userService: UserService

    @SpyBean
    private lateinit var passwordEncoder: PasswordEncoder

    @Test
    @DisplayName("회원 가입 정상 테스트")
    fun register_ValidUser() {

        // given
        val user = User(null,"01012345678",null, null, "이름", 1)
        val authData = SmsAuthentication(null, "123456", "01012345678", true)
        val regUser = User(name = user.name, password = "", userId= "", cellphone = user.cellphone, verificationId = user.verificationId)

        given(authRepository.findById(user.verificationId)).willReturn(authData)
        given(userRepository.findByCellphone(user.cellphone)).willReturn(null)
        given(userRepository.save(regUser)).willReturn(regUser)

        // when
        val result = userService.register(user)

        // then
        assertNotNull(result)
        assertEquals(user.name, result!!.name)
        assertEquals(user.cellphone, result.cellphone)
        assertEquals(user.verificationId, result.verificationId)

    }



    @Test
    @DisplayName("회원 가입 검증 테스트 - 이름/휴대번호 공란")
    fun register_InvalidNameOrCellphone_ThrowsIllegalArgumentException() {

        // given
        val user = User(null,"",null, null, "", 1)

        // when
        val exception: Throwable = assertThrows(IllegalArgumentException::class.java) { userService.register(user) }

        // then
        assertEquals("이름, 휴대전화번호를 모두 입력하세요.", exception.message)

    }


    @Test
    @DisplayName("회원 가입 검증 테스트 - 인증 데이터 실패")
    fun register_NoData_ThrowsIllegalArgumentException() {

        // given
        val user = User(null,"01082828282",null, null, "테스트", 9)

        // when
        val exception: Throwable = assertThrows(IllegalArgumentException::class.java) { userService.register(user) }

        // then
        assertEquals("인증 데이터를 찾을 수 없습니다.", exception.message)

    }



    @Test
    @DisplayName("회원 가입 검증 테스트 - 인증 요청 / 가입 요청 휴대폰 번호 상이")
    fun register_DifferentCellphone_ThrowsIllegalArgumentException() {

        // given
        val user = User(null,"01082828282",null, null, "테스트", 3)
        val authData = SmsAuthentication(null, "123456", "01012345678", false)

        given(authRepository.findById(user.verificationId)).willReturn(authData)

        // when
        val exception: Throwable = assertThrows(IllegalArgumentException::class.java) { userService.register(user) }

        // then
        assertEquals("인증을 요청한 휴대폰 번호와 가입 요청 휴대폰 번호가 다릅니다.", exception.message)

    }


    @Test
    @DisplayName("회원 가입 검증 테스트 - 휴대폰 인증 미진행")
    fun register_NoAuth_ThrowsIllegalArgumentException() {

        // given
        val user = User(null,"01012345678",null, null, "테스트", 1)
        val authData = SmsAuthentication(null, "123456", "01012345678", false)

        given(authRepository.findById(user.verificationId)).willReturn(authData)

        // when
        val exception: Throwable = assertThrows(IllegalArgumentException::class.java) { userService.register(user) }

        // then
        assertEquals("휴대폰 인증이 진행되지 않았습니다.", exception.message)

    }



    @Test
    @DisplayName("로그인 검증 테스트 - 아이디 미 존재")
    fun login_NoId_ThrowsEntityNotFoundException() {

        // given
        val user = User(null,"","testId", "", "", -1)

        given(userRepository.findByUserId(user.userId!!)).willReturn(null)

        // when
        val exception: Throwable = assertThrows(EntityNotFoundException::class.java) { userService.login(user) }

        // then
        assertEquals("해당 아이디로 가입된 사용자가 없습니다.", exception.message)

    }


    @Test
    @DisplayName("로그인 검증 테스트 - 비밀번호 불일치")
    fun login_IncorrectPw_ThrowsIllegalArgumentException() {

        // given
        val user = User(null,"01012345678","testId", "testPassword", "이름", 1)
        val dbUser = User(null,"01012345678","testId", "testPassword22", "이름", 1)

        given(userRepository.findByUserId(user.userId!!)).willReturn(dbUser)

        // when
        val exception: Throwable = assertThrows(IllegalArgumentException::class.java) { userService.login(user) }

        // then
        assertEquals("비밀번호가 일치하지 않습니다.", exception.message)

    }



    @Test
    @DisplayName("로그인 정상 테스트")
    fun login_ValidUser() {

        // given
        val user = User(name = "이름", userId= "testId", password= "testPassword", cellphone = "01012345678", verificationId = 1)

        given(userRepository.findByUserId(user.userId!!)).willReturn(user)
        given(passwordEncoder.matches(user.password, user.password)).willReturn(true)

        // when
        val result = userService.login(user)

        // then
        assertNotNull(result)
        assertEquals(user.name, result!!.name)
        assertEquals(user.cellphone, result.cellphone)
        assertEquals(user.verificationId, result.verificationId)

    }



}