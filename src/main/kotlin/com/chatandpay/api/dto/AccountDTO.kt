package com.chatandpay.api.dto

import com.chatandpay.api.common.ApiResponse
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import javax.validation.constraints.Min
import javax.validation.constraints.Pattern
import javax.validation.constraints.Positive
import javax.validation.constraints.Size

class AccountDTO {

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class OtherBankAccountRequestDTO(
        @field:Size(min = 3, max = 3, message = "은행 코드는 3자리의 숫자입니다.")
        val bankCode: String,
        @field:Pattern(regexp = "^[0-9]*\$", message = "계좌는 숫자만 입력할 수 있습니다.")
        @field:Size(min = 8, max = 20, message = "계좌번호는 8~20자 사이입니다.")
        val accountNumber: String,
        @field:Size(min = 1, message = "계좌별칭은 1자 이상 입력해야 합니다.")
        val accountName: String,
        @field:Size(min = 1, max = 1, message = "Input Error: autoDebitAgree - Y/N")
        val autoDebitAgree: String,
        @field:Positive(message = "Input Error: userId 미입력")
        val userId: Long,
    )


    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class OtherBankAccountResponseDTO (
        val bankCode: String,
        val accountNumber: String,
        val accountName: String,
        val autoDebitAgree: String,
    ) : ApiResponse()


    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class DepositWalletRequestDTO (
        @field:Min(value = 1, message = "1원보다 큰 금액만 충전할 수 있습니다.")
        val depositMoney: Int,
        @field:Positive(message = "Input Error: accountId 미입력")
        val accountId: Long,
    )


    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class DepositWalletResponseDTO (
        val chargeAmount : Int,
        val walletAmount : Int
    ) : ApiResponse()

}