package com.chatandpay.api.service

import com.chatandpay.api.domain.PayUser
import com.chatandpay.api.domain.User
import com.chatandpay.api.domain.Wallet
import com.chatandpay.api.dto.SignUpPayUserDTO
import com.chatandpay.api.repository.PayUserRepository
import com.chatandpay.api.repository.UserRepository
import com.chatandpay.api.repository.WalletRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.mockito.Mockito.*
import javax.persistence.EntityNotFoundException

@SpringBootTest
@ExtendWith(MockitoExtension::class)
class PayUserServiceTests {

    @MockBean
    private lateinit var payUserRepository: PayUserRepository

    @MockBean
    private lateinit var userRepository: UserRepository

    @MockBean
    private lateinit var walletRepository : WalletRepository

    @Autowired
    private lateinit var payUserService: PayUserService

    @BeforeEach
    fun setup() {
        reset(payUserRepository)
        reset(userRepository)
        reset(walletRepository)
    }


    @Test
    @DisplayName("페이 사용자 가입 검증 테스트 - 기 가입 CI")
    fun register_ExistingUser_ThrowsIllegalArgumentException() {

        // given
        val clientInput = SignUpPayUserDTO("ci", 1, "userSeqNo", "20230501")

        val user = User(1, "01012121212", "", "", "이름", 1)
        val payUser = PayUser(1, clientInput.ci, user, clientInput.userSeqNo , clientInput.birthDate, null)
        val wallet = Wallet(1, 100, payUser)
        payUser.wallet = wallet

        given(payUserRepository.findByCi(payUser.ci)).willReturn(payUser)

        // when
        val exception: Throwable = assertThrows(IllegalArgumentException::class.java) { payUserService.register(clientInput) }

        // then
        assertEquals("기 가입 유저입니다.", exception.message)

    }

    @Test
    @DisplayName("페이 사용자 가입 검증 테스트 - 미 존재 회원")
    fun register_NonExistingUser_ThrowsEntityNotFoundException() {

        // given
        val clientInput = SignUpPayUserDTO("ci", 1, "userSeqNo", "20230501")


        given(payUserRepository.findByCi(clientInput.ci)).willReturn(null)
        given(userRepository.findById(clientInput.userId)).willReturn(null)


        // when
        val exception: Throwable = assertThrows(IllegalArgumentException::class.java) { payUserService.register(clientInput) }

        // then
        assertEquals("존재하지 않는 유저입니다.", exception.message)

    }


    @Test
    @DisplayName("페이 사용자 가입 정상 테스트")
    fun register_ValidUser() {

        // given
        val clientInput = SignUpPayUserDTO("ci", 1, "userSeqNo", "20000101")

        val user = User(1, "01012121212", "", "", "이름", 1)
        val payUser = PayUser(1, clientInput.ci, user, clientInput.userSeqNo, clientInput.birthDate, null)
        val wallet = Wallet(1, 100, payUser)
        payUser.wallet = wallet

        given(payUserRepository.findByCi(clientInput.ci)).willReturn(null)
        given(userRepository.findById(clientInput.userId)).willReturn(user)

        val regUser = PayUser(ci = payUser.ci, user = user, userSeqNo = payUser.userSeqNo, wallet = null, birthDate = payUser.birthDate)
        given(payUserRepository.save(regUser)).willReturn(payUser)

        val savedWallet = Wallet(money = 0, payUser = payUser)
        given(walletRepository.save(savedWallet)).willReturn(wallet)

        // when
        val result = payUserService.register(clientInput)

        // then
        assertNotNull(result)
        assertEquals(result?.userSeqNo, clientInput.userSeqNo)

    }


    @Test
    @DisplayName("페이 사용자 탈퇴 검증 테스트 - 존재하지 않는 사용자 IDX")
    fun withdrawPayService_NonExistingUser_ThrowsEntityNotFoundException() {

        // given
        val userId : Long = 2
        given(payUserRepository.findByUserId(userId)).willReturn(null)

        // when
        val exception: Throwable = assertThrows(EntityNotFoundException::class.java) { payUserService.withdrawPayService(userId) }

        // then
        assertEquals("IDX 입력이 잘못되었습니다.", exception.message)

    }

    @Test
    @DisplayName("페이 사용자 탈퇴 검증 테스트 - 탈퇴 실패: 지갑 삭제 실패")
    fun withdrawPayService_Failure_ThrowsWalletRestApiException() {

        // given
        val user = User(1, "01012121212", "", "", "이름", 1)
        val payUser = PayUser(1, "testCi", user, "testUserSeqNo", "20230501", null)
        val wallet = Wallet(1, 100, payUser)
        payUser.wallet = wallet

        given(payUserRepository.findByUserId(payUser.id!!)).willReturn(payUser)
        given(walletRepository.findByPayUserId(payUser.id!!)).willReturn(payUser.wallet)
        given(walletRepository.delete(payUser.wallet!!)).willReturn(false)

        // when
        val result = payUserService.withdrawPayService(payUser.id!!)

        // then
        assertEquals(false, result)

    }


    @Test
    @DisplayName("페이 사용자 탈퇴 검증 테스트 - 탈퇴 실패: 지갑 삭제 실패")
    fun withdrawPayService_Failure_ThrowsRestApiException() {

        // given
        val user = User(1, "01012121212", "", "", "이름", 1)
        val payUser = PayUser(1, "testCi", user, "testUserSeqNo", "20230501", null)
        val wallet = Wallet(1, 100, payUser)
        payUser.wallet = wallet

        given(payUserRepository.findByUserId(payUser.id!!)).willReturn(payUser)
        given(walletRepository.findByPayUserId(payUser.id!!)).willReturn(payUser.wallet)
        given(walletRepository.delete(payUser.wallet!!)).willReturn(true)
        given(payUserRepository.delete(payUser)).willReturn(false)

        // when
        val result = payUserService.withdrawPayService(payUser.id!!)

        // then
        assertEquals(false, result)


    }


    @Test
    @DisplayName("페이 유저 확인 정상 테스트")
    fun isPayUser_PayUser_ReturnsTrue() {

        // given
        val user = User(1, "test-user-id", "", "", "이름", 1)
        val payUser = PayUser(1, "testCi", user, "testUserSeqNo", "20230501", null)
        val wallet = Wallet(1, 100, payUser)
        payUser.wallet = wallet

        given(payUserRepository.findByUserId(payUser.id!!)).willReturn(payUser)

        // when
        val result = payUserService.isPayUser(payUser.id!!)

        // then
        assertTrue(result)

    }

    @Test
    @DisplayName("페이 유저 확인 검증 테스트 - PayUser가 아닌 경우")
    fun isPayUser_NonPayUser_ReturnsFalse() {
        // given
        val userId : Long = 2
        given(payUserRepository.findByUserId(userId)).willReturn(null)

        // when
        val result = payUserService.isPayUser(userId)

        // then
        assertFalse(result)
    }



    @Test
    @DisplayName("지갑 입금 정상 테스트")
    fun depositintoWallet_ValidInput_Success() {

        // given
        val user = User(1, "01012121212", "", "", "이름", 1)
        val payUser = PayUser(1, "testCi", user, "testUserSeqNo", "20230501", null)
        val wallet = Wallet(1, 100, payUser)
        payUser.wallet = wallet

        val updatesMoney = 100
        given(walletRepository.save(payUser.wallet!!)).willReturn(payUser.wallet)
        given(payUserRepository.save(payUser)).willReturn(payUser)


        // when
        val result = payUserService.depositintoWallet(updatesMoney, payUser)

        // then
        assertNotNull(result)
        assertEquals(200, result?.wallet?.money)

    }

}