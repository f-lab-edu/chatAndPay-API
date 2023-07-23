package com.chatandpay.api.dto

import com.chatandpay.api.common.ApiResponse
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.util.*
import javax.validation.constraints.Min
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

class TransferDTO {

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class ReceiveTransferRequestDTO(

        val senderId: Long,
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
        @field:Pattern(regexp = "^[1-9]*\$", message =  "Type Error: accountNumber - 송신계좌")
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

        val senderId: Long,
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
        val requestUserId: Long,
        val changeType: String,
    )

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class ChangePendingTransferResponseDTO(

        var isSucceed: Boolean,
        var remainPendingTransfer: Int,

    ) : ApiResponse()


}