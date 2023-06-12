package com.chatandpay.api.service

import com.chatandpay.api.code.BankCode
import com.chatandpay.api.domain.OtherBankAccount
import com.chatandpay.api.domain.PayUser
import com.chatandpay.api.domain.User
import com.chatandpay.api.domain.Wallet
import com.chatandpay.api.dto.OpenApiDepositWalletDTO
import com.chatandpay.api.repository.AccountRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean

@SpringBootTest
@ExtendWith(MockitoExtension::class)
class AccountServiceTests {

    @Autowired
    private lateinit var accountService: AccountService

    @MockBean
    private lateinit var accountRepository: AccountRepository

    @MockBean
    private lateinit var payUserService: PayUserService

    @Test
    @DisplayName("계좌 저장 정상 테스트")
    fun saveAccount_ValidUser() {

        // given
        val user = User(1, "01012121212", "", "", "이름", 1)
        val payUser = PayUser(1, "ci", user, "userSeqNo" , "20000101", null)
        val wallet = Wallet(1, 100, payUser)
        payUser.wallet = wallet

        val dto = OtherBankAccount(null, BankCode.BK001.bankCode, "123412341234", "이름", "Y", payUser)

        given(accountRepository.save(dto)).willReturn(dto)

        // when
        val result = accountService.saveAccount(dto)

        // then
        assertEquals(dto, result)

    }



    @Test
    @DisplayName("계좌 충전 정상 테스트")
    fun chargeWallet_ValidUser() {

        // given
        val user = User(1, "01012121212", "", "", "이름", 1)
        val payUser = PayUser(1, "ci", user, "userSeqNo" , "20000101", null)
        val wallet = Wallet(1, 100, payUser)
        payUser.wallet = wallet

        val dto = OpenApiDepositWalletDTO(100, 3L, payUser)

        given(payUserService.depositintoWallet(dto.depositMoney, dto.payUser)).willReturn(payUser)

        // when
        val result = accountService.chargeWallet(dto)

        // then
        assertEquals(dto.depositMoney, result.depositMoney)
        assertEquals(dto.accountId, result.accountId)
        assertEquals(payUser, result.payUser)

    }

}