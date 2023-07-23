package com.chatandpay.api.dto

import com.chatandpay.api.common.ApiResponse
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.util.*

class TransferDTO {

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class ReceiveTransferRequestDTO(

        val senderId: Long,
        val receiverId: Long,
        val amount: Int

    ): ApiResponse()

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
        val bankCode: String,
        val accountNumber: String,
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