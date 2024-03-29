package com.chatandpay.api.dto

import com.chatandpay.api.common.ApiResponse
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.util.*
import javax.validation.constraints.Min
import javax.validation.constraints.Pattern
import javax.validation.constraints.Positive
import javax.validation.constraints.Size

class TransferDTO {

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class ReceiveTransferRequestDTO(

        @field:Positive(message = "Input Error: senderId 미입력")
        val senderId: Long,
        @field:Positive(message = "Input Error: receiverId 미입력")
        val receiverId: Long,
        @field:Min(value = 1, message = "1원보다 큰 금액만 송금할 수 있습니다.")
        val amount: Int

    )

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class ReceiveTransferResponseDTO(

        val transferUuid: UUID,
        val sendingAmount: Int,
        val walletAmount: Int

    ) : ApiResponse()


    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class SendTransferResponseDTO (

        val transferUuid: UUID,
        val receivedAmount: Int,
        val walletAmount: Int,

    ) : ApiResponse()


    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class OtherBankTransferRequestDTO(

        val senderId: Long,
        @field:Size(min = 3, max = 3, message = "은행 코드는 3자리의 숫자입니다.")
        val bankCode: String,
        @field:Pattern(regexp = "^[0-9]*\$", message = "계좌는 숫자만 입력할 수 있습니다.")
        @field:Size(min = 8, max = 20, message = "계좌번호는 8~20자 사이입니다.")
        val accountNumber: String,
        @field:Min(value = 1, message = "1원보다 큰 금액만 송금할 수 있습니다.")
        val amount: Int

    )

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class OtherBankTransferResponseDTO(

        val transferUuid: UUID,
        val isSucceeded: Boolean,
        val sendingAmount: Int,
        val walletAmount: Int

    ) : ApiResponse()

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class RegOtherBankTransferRequestDTO(

        @field:Positive(message = "Input Error: senderId 미입력")
        val senderId: Long,
        @field:Positive(message = "Input Error: senderBankAccount 미입력")
        val senderBankAccount: Long,
        @field:Min(value = 1, message = "1원보다 큰 금액만 송금할 수 있습니다.")
        val amount: Int

    )

    data class PendingTransferDTO (

        val uuid: UUID,
        val sender: Long,
        val receiver: Long,
        val amount: Int

    )


    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class ChangePendingTransferRequestDTO(

        val uuid: UUID,
        @field:Positive(message = "Input Error: userId 미입력")
        val requestUserId: Long,
        val changeType: String,
    )

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class ChangePendingTransferResponseDTO(

        var isSucceed: Boolean,
        var remainPendingTransfer: Int,

    ) : ApiResponse()


}