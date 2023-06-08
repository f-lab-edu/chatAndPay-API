package com.chatandpay.api

import com.chatandpay.api.domain.PayUser
import com.chatandpay.api.domain.User
import com.chatandpay.api.domain.Wallet
import com.chatandpay.api.domain.sms.SmsAuthentication
import com.chatandpay.api.dto.AuthConfirmRequestDTO
import com.chatandpay.api.repository.AuthRepository
import com.chatandpay.api.repository.UserRepository
import com.chatandpay.api.service.PayUserService
import com.chatandpay.api.service.SmsService
import com.chatandpay.api.service.UserService
import com.chatandpay.api.utils.RandomUtil
import io.mockk.every
import io.mockk.mockkObject
import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.security.crypto.password.PasswordEncoder
import javax.persistence.EntityNotFoundException


@SpringBootTest
@ExtendWith(MockitoExtension::class)
class UserServiceTests {

    @MockBean
    private lateinit var authRepository: AuthRepository

    @MockBean
    private lateinit var userRepository: UserRepository

    @MockBean
    private lateinit var payUserService: PayUserService

    @Autowired
    private lateinit var userService: UserService

    @SpyBean
    private lateinit var passwordEncoder: PasswordEncoder

    @SpyBean
    private lateinit var smsService: SmsService

    @BeforeEach
    fun setup() {
        mockkObject(RandomUtil)
        reset(userRepository)
        reset(authRepository)
    }

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


    @Test
    @DisplayName("회원 가입 SMS 인증 검증 테스트 - 기 가입 번호")
    fun authJoin_ExistingUser_ThrowsIllegalArgumentException() {

        // given
        val user = User(name = "이름", userId= "testId", password= "testPassword", cellphone = "01012345678", verificationId = 1)
        given(userRepository.findByCellphone(user.cellphone)).willReturn(user)

        // when
        val exception: Throwable = assertThrows(IllegalArgumentException::class.java) { userService.authJoin(user) }

        // then
        assertEquals("해당 휴대전화번호로 가입된 사용자가 있습니다.", exception.message)

    }

    @Test
    @DisplayName("회원 가입 SMS 인증 정상 테스트")
    fun authJoin_ValidUser() {

        // given
        val user = User(name = "이름", userId= "testId", password= "testPassword", cellphone = "01012345678", verificationId = 1)
        given(userRepository.findByCellphone(user.cellphone)).willReturn(null)

        // given(randomUtilMockedStatic.generateRandomSixDigits()).willReturn("123456")
        every { RandomUtil.generateRandomSixDigits() } returns "123456"

        val receiveSmsAuth = SmsAuthentication(1, "123456", user.cellphone, false)
        val sendSmsAuth = SmsAuthentication(null, receiveSmsAuth.authNumber, receiveSmsAuth.phoneNumber)
        given(authRepository.save(sendSmsAuth)).willReturn(receiveSmsAuth)
        doReturn(receiveSmsAuth.id!!).`when`(smsService).authSendSms(user.cellphone)

        // when
        val result = userService.authJoin(user)

        // then
        assertNotNull(result)

    }


    @Test
    @DisplayName("로그인 SMS 인증 요청 검증 테스트 - 미가입 번호")
    fun authLogin_ExistingUser_ThrowsEntityNotFoundException() {

        // given
        val user = User(name = "이름", userId= "testId", password= "testPassword", cellphone = "01012345678", verificationId = 1)
        given(userRepository.findByCellphone(user.cellphone)).willReturn(null)

        // when
        val exception: Throwable = assertThrows(EntityNotFoundException::class.java) { userService.authLogin(user.cellphone) }

        // then
        assertEquals("해당 휴대전화번호로 가입된 사용자가 없습니다.", exception.message)

    }

    @Test
    @DisplayName("로그인 SMS 인증 요청 정상 테스트")
    fun authLogin_ValidUser() {


        // given
        val user = User(name = "이름", userId= "testId", password= "testPassword", cellphone = "01012345678", verificationId = 1)
        given(userRepository.findByCellphone(user.cellphone)).willReturn(user)

        //given(RandomUtil.generateRandomSixDigits()).willReturn("123456")
        every { RandomUtil.generateRandomSixDigits() } returns "123456"

        val receiveSmsAuth = SmsAuthentication(1, "123456", user.cellphone, false)
        val sendSmsAuth = SmsAuthentication(null, receiveSmsAuth.authNumber, receiveSmsAuth.phoneNumber)
        given(authRepository.save(sendSmsAuth)).willReturn(receiveSmsAuth)
        doReturn(receiveSmsAuth.id!!).`when`(smsService).authSendSms(user.cellphone)

        // when
        val result = userService.authLogin(user.cellphone)

        // then
        assertNotNull(result)
        assertEquals(user.name, result!!.name)
        assertEquals(user.cellphone, result.cellphone)
        assertEquals(user.verificationId, result.verificationId)

    }


    @Test
    @DisplayName("로그인 SMS 인증 확인 검증 테스트 - 미가입 번호")
    fun authLoginConfirm_ExistingUser_ThrowsIllegalArgumentException() {

        // given
        val trySmsAuth = SmsAuthentication(null, "123456", "01042424242")
        val givenSmsAuth = SmsAuthentication(null, "123456", "01042424242", false)
        val authConfirm = AuthConfirmRequestDTO("이름", "01042424242", "123456")

        given(authRepository.findByCellphone(authConfirm.cellphone)).willReturn(givenSmsAuth)
        given(smsService.authSendSmsConfirm(trySmsAuth)).willReturn(givenSmsAuth)
        given(userRepository.findByCellphone(authConfirm.cellphone)).willReturn(null)

        // when
        val exception: Throwable = assertThrows(EntityNotFoundException::class.java) { userService.authLoginConfirm(authConfirm) }

        // then
        assertEquals("해당 휴대전화번호로 가입된 사용자가 없습니다.", exception.message)

    }


    @Test
    @DisplayName("로그인 SMS 인증 확인 검증 테스트 - 존재하지 않는 휴대전화번호")
    fun authLoginConfirm_NonExistingCellphone_ThrowsEntityNotFoundException() {

        // TODO - 내부 authConfirm 호출 건 : 필요 여부 확인 필요

        // given
        val authConfirm = AuthConfirmRequestDTO("이름", "01012341234", "123456")
        given(authRepository.findByCellphone(authConfirm.cellphone)).willReturn(null)

        // when
        val exception: Throwable = assertThrows(EntityNotFoundException::class.java) {
            userService.authConfirm(authConfirm)
        }

        // then
        assertEquals("해당 휴대전화번호로 요청된 인증이 없습니다.", exception.message)
    }


    @Test
    @DisplayName("인증 확인 검증 테스트 - 올바르지 않은 인증 번호")
    fun authLoginConfirm_InvalidAuthNumber_ThrowsIllegalArgumentException() {

        // TODO - 내부 authConfirm 호출 건 : 필요 여부 확인 필요

        // given
        val smsAuth = SmsAuthentication(1, "123456", "01012341234", false)
        val authCfmReq = AuthConfirmRequestDTO(smsAuth.phoneNumber, "01012341234", "444444")

        given(authRepository.findByCellphone(smsAuth.phoneNumber)).willReturn(smsAuth)

        // when
        val exception: Throwable = assertThrows(IllegalArgumentException::class.java) {
            userService.authConfirm(authCfmReq)
        }

        // then
        assertEquals("입력한 인증 문자가 일치하지 않습니다.", exception.message)
    }


    @Test
    @DisplayName("인증 확인 검증 테스트 - 이미 처리된 인증")
    fun authLoginConfirm_AlreadyVerifiedAuth_ThrowsIllegalArgumentException() {

        // TODO - 내부 authConfirm 호출 건 : 필요 여부 확인 필요

        // given
        val smsAuth = SmsAuthentication(1, "123456", "01012341234", true)
        val authCfmReq = AuthConfirmRequestDTO(smsAuth.phoneNumber, "01012341234", "123456")

        given(authRepository.findByCellphone(smsAuth.phoneNumber)).willReturn(smsAuth)


        // when
        val exception: Throwable = assertThrows(IllegalArgumentException::class.java) {
            userService.authConfirm(authCfmReq)
        }

        // then
        assertEquals("이미 처리된 인증입니다.", exception.message)
    }


    @Test
    @DisplayName("로그인 SMS 인증 확인 정상 테스트")
    fun authLoginConfirm_ValidUser() {

        // given
        val user = User(name = "이름", userId= "testId", password= "testPassword", cellphone = "01012345678", verificationId = 1)
        given(userRepository.findByCellphone(user.cellphone)).willReturn(user)

        //given(RandomUtil.generateRandomSixDigits()).willReturn("123456")
        every { RandomUtil.generateRandomSixDigits() } returns "123456"

        val receiveSmsAuth = SmsAuthentication(1, "123456", user.cellphone, false)
        val sendSmsAuth = SmsAuthentication(null, receiveSmsAuth.authNumber, receiveSmsAuth.phoneNumber)
        given(authRepository.save(sendSmsAuth)).willReturn(receiveSmsAuth)
        doReturn(receiveSmsAuth.id!!).`when`(smsService).authSendSms(user.cellphone)

        // when
        val result = userService.authLogin(user.cellphone)

        // then
        assertNotNull(result)
        assertEquals(user.name, result!!.name)
        assertEquals(user.cellphone, result.cellphone)
        assertEquals(user.verificationId, result.verificationId)

    }



    @Test
    @DisplayName("인증 확인 검증 테스트 - 존재하지 않는 휴대전화번호")
    fun authConfirm_NonExistingCellphone_ThrowsEntityNotFoundException() {

        // given
        val authConfirm = AuthConfirmRequestDTO("이름", "01012341234", "123456")
        given(authRepository.findByCellphone(authConfirm.cellphone)).willReturn(null)

        // when
        val exception: Throwable = assertThrows(EntityNotFoundException::class.java) {
            userService.authConfirm(authConfirm)
        }

        // then
        assertEquals("해당 휴대전화번호로 요청된 인증이 없습니다.", exception.message)
    }


    @Test
    @DisplayName("인증 확인 검증 테스트 - 올바르지 않은 인증 번호")
    fun authConfirm_InvalidAuthNumber_ThrowsIllegalArgumentException() {

        // given
        val smsAuth = SmsAuthentication(1, "123456", "01012341234", false)
        val authCfmReq = AuthConfirmRequestDTO(smsAuth.phoneNumber, "01012341234", "444444")

        given(authRepository.findByCellphone(smsAuth.phoneNumber)).willReturn(smsAuth)

        // when
        val exception: Throwable = assertThrows(IllegalArgumentException::class.java) {
            userService.authConfirm(authCfmReq)
        }

        // then
        assertEquals("입력한 인증 문자가 일치하지 않습니다.", exception.message)
    }


    @Test
    @DisplayName("인증 확인 검증 테스트 - 이미 처리된 인증")
    fun authConfirm_AlreadyVerifiedAuth_ThrowsIllegalArgumentException() {

        // given
        val smsAuth = SmsAuthentication(1, "123456", "01012341234", true)
        val authCfmReq = AuthConfirmRequestDTO(smsAuth.phoneNumber, "01012341234", "123456")

        given(authRepository.findByCellphone(smsAuth.phoneNumber)).willReturn(smsAuth)


        // when
        val exception: Throwable = assertThrows(IllegalArgumentException::class.java) {
            userService.authConfirm(authCfmReq)
        }

        // then
        assertEquals("이미 처리된 인증입니다.", exception.message)
    }



    @Test
    @DisplayName("인증 확인 정상 테스트")
    fun authConfirm_ValidUser() {

        // TODO 필요 여부 파악 필요 - verify도 들어가기 모호한 위치로 보여짐

        // given
        val trySmsAuth = SmsAuthentication(null, "121212", "01012121234")
        val givenSmsAuth = SmsAuthentication(null, "121212", "01012121234", false)
        val authCfmReq = AuthConfirmRequestDTO(trySmsAuth.phoneNumber, "01012121234", "121212")

        given(authRepository.findByCellphone(trySmsAuth.phoneNumber)).willReturn(givenSmsAuth)
        given(smsService.authSendSmsConfirm(trySmsAuth)).willReturn(givenSmsAuth)

        // when
        val result = userService.authConfirm(authCfmReq)

        // then
        assertNotNull(result)
        assertEquals(trySmsAuth.id, result.id)
        assertTrue(trySmsAuth.isVerified)

    }


    @Test
    @DisplayName("사용자 업데이트 검증 테스트 - 존재하지 않는 사용자 ID")
    fun updateUser_NonuserId_ThrowsEntityNotFoundException() {

        // given
        val user = User(name = "이름", userId= "testId", password= "testPassword", cellphone = "01012345678", verificationId = 1)
        val idx : Long = -1

        given(userRepository.findById(idx)).willReturn(null)

        // when
        val exception: Throwable = assertThrows(EntityNotFoundException::class.java) {
            userService.updateUser(idx, user)
        }

        // then
        assertEquals("IDX 입력이 잘못되었습니다.", exception.message)

    }

    @Test
    @DisplayName("사용자 업데이트 검증 테스트 - 이미 존재하는 아이디")
    fun updateUser_ExistingUserId_ThrowsIllegalArgumentException() {

        // given
        val user = User(name = "이름", userId= "testId", password= "testPassword", cellphone = "01012345678", verificationId = 1)
        val idx : Long = 1

        given(userRepository.findById(idx)).willReturn(user)
        given(userRepository.existsByUserIdAndIdNot(user.userId!!, idx)).willReturn(true)

        // when
        val exception: Throwable = assertThrows(IllegalArgumentException::class.java) {
            userService.updateUser(idx, user)
        }

        // then
        assertEquals("이미 존재하는 아이디입니다.", exception.message)
    }

    @Test
    @DisplayName("사용자 업데이트 검증 테스트 - 이미 존재하는 전화번호")
    fun updateUser_ExistingCellphone_ThrowsIllegalArgumentException() {

        // given
        val user = User(name = "이름", userId= "testId", password= "testPassword", cellphone = "01012345678", verificationId = 1)
        val idx : Long = 1

        given(userRepository.findById(idx)).willReturn(user)
        given(userRepository.existsByCellphoneAndIdNot(user.cellphone, idx)).willReturn(true)

        // when
        val exception: Throwable = assertThrows(IllegalArgumentException::class.java) {
            userService.updateUser(idx, user)
        }

        // then
        assertEquals("이미 존재하는 전화번호입니다.", exception.message)

    }

    @Test
    @DisplayName("사용자 업데이트 검증 테스트 - ID와 패스워드 동시 등록")
    fun updateUser_RegisterIdAndPassword_Success() {

        // given
        val idx : Long = 1
        val user = User(name = "이름", userId= "", password= "", cellphone = "01012345678", verificationId = 1)
        val newUser = User(name = "이름", userId= "testId", password= "testPassword", cellphone = "01012345678", verificationId = 1)

        newUser.userId = "newUserId"
        newUser.password = "newPassword"
        newUser.password = passwordEncoder.encode(newUser.password)

        given(userRepository.findById(idx)).willReturn(user)
        given(userRepository.save(user)).willReturn(newUser)

        // when
        val result = userService.updateUser(idx, newUser)

        // then
        assertEquals(newUser.userId, result!!.userId)
        assertEquals(newUser.password, result.password)

    }

    @Test
    @DisplayName("사용자 업데이트 검증 테스트 - 패스워드 업데이트")
    fun updateUser_UpdatePassword_Success() {

        // given
        val idx : Long = 1
        val user = User(name = "이름", userId= "testId", password= "testPassword", cellphone = "01012345678", verificationId = 1)
        val newUser = User(name = "이름", userId= "testId", password= "testPassword", cellphone = "01012345678", verificationId = 1)
        newUser.password = "newPassword"
        newUser.password = passwordEncoder.encode(newUser.password)

        given(userRepository.findById(idx)).willReturn(user)
        given(userRepository.save(user)).willReturn(newUser)


        // when
        val result = userService.updateUser(idx, newUser)

        // then
        assertEquals(newUser.password, result!!.password)

    }

    @Test
    @DisplayName("사용자 업데이트 검증 테스트 - 전화번호 업데이트")
    fun updateUser_UpdateCellphone_Success() {

        // given
        val idx : Long = 1
        val user = User(name = "이름", userId= "testId", password= "testPassword", cellphone = "01012345678", verificationId = 1)
        val newUser = User(name = "이름", userId= "testId", password= "testPassword", cellphone = "01012345678", verificationId = 1)
        newUser.cellphone = "01012123434"
        newUser.password = newUser.password

        given(userRepository.findById(idx)).willReturn(user)
        given(userRepository.save(user)).willReturn(newUser)

        // when
        val result = userService.updateUser(idx, newUser)

        // then
        assertEquals(newUser.cellphone, result!!.cellphone)

    }




    @Test
    @DisplayName("사용자 삭제 검증 테스트 - 존재하지 않는 사용자 IDX")
    fun deleteUser_NonExistingUserId_ThrowsEntityNotFoundException() {

        // given
        val idx : Long = 1
        given(userRepository.findById(idx)).willReturn(null)

        // when
        val exception: Throwable = assertThrows(EntityNotFoundException::class.java) {
            userService.deleteUser(idx)
        }

        // then
        assertEquals("IDX 입력이 잘못되었습니다.", exception.message)

    }


    @Test
    @DisplayName("사용자 삭제 검증 테스트 - PayUser 삭제 성공, User 삭제 성공")
    fun deleteUser_PayUser_DeleteSuccess() {
        // given
        val idx : Long = 1
        val user = User(name = "이름", userId= "testId", password= "testPassword", cellphone = "01012345678", verificationId = 1)
        val payUser = PayUser(1, "ci", user,"userSeqNo", "20230605", null)
        payUser.wallet = Wallet(null,100, payUser)

        given(userRepository.findById(idx)).willReturn(user)
        given(payUserService.isPayUser(idx)).willReturn(true)
        given(userRepository.delete(user)).willReturn(true)

        // when
        val result = userService.deleteUser(idx)

        // then
        assertTrue(result)
        verify(payUserService).withdrawPayService(idx)
        verify(userRepository).delete(user)
    }

    @Test
    @DisplayName("사용자 삭제 검증 테스트 - PayUser 미존재, User 삭제 성공")
    fun deleteUser_NonPayUser_DeleteSuccess() {
        // given
        val idx : Long = 1
        val user = User(name = "이름", userId= "testId", password= "testPassword", cellphone = "01012345678", verificationId = 1)

        given(userRepository.findById(idx)).willReturn(user)
        given(payUserService.isPayUser(idx)).willReturn(false)
        given(userRepository.delete(user)).willReturn(true)

        // when
        val result = userService.deleteUser(idx)

        // then
        assertTrue(result)
        verify(payUserService, never()).withdrawPayService(idx)
        verify(userRepository).delete(user)
    }



}