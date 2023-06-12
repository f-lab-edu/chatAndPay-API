package com.chatandpay.api.service

import com.chatandpay.api.domain.*
import com.chatandpay.api.dto.ReceiveTransferRequestDTO
import com.chatandpay.api.repository.PayUserRepository
import com.chatandpay.api.repository.TransferRepository
import com.chatandpay.api.repository.WalletRepository
import io.mockk.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mockito.reset
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import java.util.*
import javax.persistence.EntityNotFoundException

@SpringBootTest
@ExtendWith(MockitoExtension::class)
class TransferServiceTests {

    @Autowired
    private lateinit var transferService: TransferService

    @MockBean
    private lateinit var payUserRepository: PayUserRepository

    @MockBean
    private lateinit var walletRepository: WalletRepository

    @MockBean
    private lateinit var transferRepository: TransferRepository

    @MockBean
    private lateinit var payUserService: PayUserService

    val mockedUuid: UUID = UUID.randomUUID()

    @BeforeEach
    fun setup() {
        reset(payUserRepository)
        reset(walletRepository)
        reset(transferRepository)

        mockkStatic(UUID::class)
        every { UUID.randomUUID() } returns mockedUuid
    }

    @Test
    @DisplayName("송금 정상 테스트")
    fun sendTransfer_ValidReq() {

        // given
        val senderUser = User(1, "01012121212", "", "", "이름", 1)
        val senderPayUser = PayUser(1, "ci", senderUser, "userSeqNo" , "20000101", null)
        val senderWallet = Wallet(1, 100, senderPayUser)
        senderPayUser.wallet = senderWallet

        val receiverUser = User(2, "01012121212", "", "", "이름", 2)
        val receiverPayUser = PayUser(2, "ci", receiverUser, "userSeqNo" , "20000101", null)
        val receiverWallet = Wallet(2, 0, receiverPayUser)
        receiverPayUser.wallet = receiverWallet

        val amount = 100
        val afterSenderWallet = Wallet(1, 0, senderPayUser)

        val transfer = Transfer(mockedUuid, senderPayUser, receiverPayUser, amount, false)

        given(payUserRepository.findById(senderPayUser.id!!)).willReturn(senderPayUser)
        given(payUserRepository.findById(receiverPayUser.id!!)).willReturn(receiverPayUser)
        given(walletRepository.save(afterSenderWallet)).willReturn(afterSenderWallet)
        given(transferRepository.save(transfer)).willReturn(transfer)

        // when
        val result = transferService.sendTransfer(ReceiveTransferRequestDTO(senderPayUser.id!!, receiverPayUser.id!!, amount))

        // then
        assertEquals(amount, result?.sendingAmount)
        assertEquals(0, result?.walletAmount)
    }

    @Test
    @DisplayName("송금 검증 테스트 - 송신자 미존재")
    fun sendTransfer_NoSender() {

        // given
        val senderUser = User(1, "01012121212", "", "", "이름", 1)
        val senderPayUser = PayUser(1, "ci", senderUser, "userSeqNo" , "20000101", null)

        val receiverUser = User(2, "01012121212", "", "", "이름", 2)
        val receiverPayUser = PayUser(2, "ci", receiverUser, "userSeqNo" , "20000101", null)

        given(payUserRepository.findById(senderPayUser.id!!)).willReturn(null)
        given(payUserRepository.findById(receiverPayUser.id!!)).willReturn(receiverPayUser)

        val amount = 100

        // when
        val exception: Throwable =
            assertThrows(EntityNotFoundException::class.java) {
                transferService.sendTransfer(ReceiveTransferRequestDTO(senderPayUser.id!!, receiverPayUser.id!!, amount))
            }

        // then
        assertEquals("송신자를 찾을 수 없습니다.", exception.message)

    }

    @Test
    @DisplayName("송금 검증 테스트 - 수신자 미존재")
    fun sendTransfer_NoReceiver() {

        // given
        val senderUser = User(1, "01012121212", "", "", "이름", 1)
        val senderPayUser = PayUser(1, "ci", senderUser, "userSeqNo" , "20000101", null)
        val senderWallet = Wallet(1, 100, senderPayUser)
        senderPayUser.wallet = senderWallet

        val receiverUser = User(2, "01012121212", "", "", "이름", 2)
        val receiverPayUser = PayUser(2, "ci", receiverUser, "userSeqNo" , "20000101", null)
        val receiverWallet = Wallet(2, 0, receiverPayUser)
        receiverPayUser.wallet = receiverWallet

        given(payUserRepository.findById(senderPayUser.id!!)).willReturn(senderPayUser)
        given(payUserRepository.findById(receiverPayUser.id!!)).willReturn(null)

        val amount = 100

        // when
        val exception: Throwable =
            assertThrows(EntityNotFoundException::class.java) {
                transferService.sendTransfer(ReceiveTransferRequestDTO(senderPayUser.id!!, receiverPayUser.id!!, amount))
            }

        // then
        assertEquals("수신자를 찾을 수 없습니다.", exception.message)

    }

    @Test
    @DisplayName("송금 검증 테스트 - 출금 잔액 부족")
    fun sendTransfer_NoMoney() {


        // given
        val senderUser = User(1, "01012121212", "", "", "이름", 1)
        val senderPayUser = PayUser(1, "ci", senderUser, "userSeqNo" , "20000101", null)
        val senderWallet = Wallet(1, 100, senderPayUser)
        senderPayUser.wallet = senderWallet

        val receiverUser = User(2, "01012121212", "", "", "이름", 2)
        val receiverPayUser = PayUser(2, "ci", receiverUser, "userSeqNo" , "20000101", null)
        val receiverWallet = Wallet(2, 0, receiverPayUser)
        receiverPayUser.wallet = receiverWallet

        given(payUserRepository.findById(senderPayUser.id!!)).willReturn(senderPayUser)
        given(payUserRepository.findById(receiverPayUser.id!!)).willReturn(receiverPayUser)

        val amount = 1000000000

        // when
        val exception: Throwable =
            assertThrows(IllegalArgumentException::class.java) {
                transferService.sendTransfer(ReceiveTransferRequestDTO(senderPayUser.id!!, receiverPayUser.id!!, amount))
            }

        // then
        assertEquals("출금 잔액이 부족합니다.", exception.message)

    }


    @Test
    @DisplayName("수금 정상 테스트")
    fun receiveTransfer_ValidReq() {

        // given
        val senderUser = User(1, "01012121212", "", "", "이름", 1)
        val senderPayUser = PayUser(1, "ci", senderUser, "userSeqNo" , "20000101", null)

        val receiverUser = User(2, "01012121212", "", "", "이름", 2)
        val receiverPayUser = PayUser(2, "ci", receiverUser, "userSeqNo" , "20000101", null)
        val receiverWallet = Wallet(2, 0, receiverPayUser)
        receiverPayUser.wallet = receiverWallet

        val amount = 100
        val receivedPayUser = PayUser(2, "ci", receiverUser, "userSeqNo" , "20000101", null)
        val receivedWallet = Wallet(2, amount, receiverPayUser)
        receivedPayUser.wallet = receivedWallet

        val beforeTransfer = Transfer(mockedUuid, senderPayUser, receiverPayUser, amount, false)
        val duringTransfer = Transfer(mockedUuid, senderPayUser, receiverPayUser, amount, true)
        val transfer = Transfer(mockedUuid, senderPayUser, receivedPayUser, amount, true)

        given(transferRepository.findById(mockedUuid)).willReturn(beforeTransfer)
        given(payUserService.depositintoWallet(transfer.amount, receiverPayUser)).willReturn(receivedPayUser)
        given(transferRepository.save(duringTransfer)).willReturn(transfer)

        // when
        val result = transferService.receiveTransfer(mockedUuid)

        // then
        assertEquals(result.transferUuid, mockedUuid)

        println("result.receivedAmount" + result.receivedAmount)
        assertEquals(result.receivedAmount, amount)
        assertEquals(result.walletAmount, receivedWallet.money)

    }


    @Test
    @DisplayName("수금 검증 테스트 - 수신 번호 미존재")
    fun saveAccount_NoId() {

        // given
        given(transferRepository.findById(mockedUuid)).willReturn(null)

        // when
        val exception: Throwable =
            assertThrows(EntityNotFoundException::class.java) {
                transferService.receiveTransfer(mockedUuid)
            }

        // then
        assertEquals("송신 요청을 찾을 수 없습니다.", exception.message)

    }



    @Test
    @DisplayName("수금 검증 테스트 - 수신 완료 송신 요청")
    fun saveAccount_NoSender() {

        // given
        val senderUser = User(1, "01012121212", "", "", "이름", 1)
        val senderPayUser = PayUser(1, "ci", senderUser, "userSeqNo" , "20000101", null)

        val receiverUser = User(2, "01012121212", "", "", "이름", 2)
        val receiverPayUser = PayUser(2, "ci", receiverUser, "userSeqNo" , "20000101", null)
        val receiverWallet = Wallet(2, 0, receiverPayUser)
        receiverPayUser.wallet = receiverWallet

        val amount = 100

        val transfer = Transfer(mockedUuid, senderPayUser, receiverPayUser, amount, true)

        given(transferRepository.findById(mockedUuid)).willReturn(transfer)

        // when
        val exception: Throwable =
            assertThrows(IllegalArgumentException::class.java) {
                transferService.receiveTransfer(mockedUuid)
            }

        // then
        assertEquals("이미 수신 완료된 송신 요청입니다.", exception.message)

    }


}