package com.chatandpay.api.dto

import com.chatandpay.api.common.ApiResponse
import com.chatandpay.api.domain.PayUser
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

class OpenApiDTO {

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class OpenApiAccessTokenDTO (
        val accessToken: String,
        val tokenType: String,
        val expiresIn: String,
        val scope: String,
        val clientUseCode: String,
    )

    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class RealNameInquiryRequestDTO(

        var bankTranId: String,
        val bankCodeStd: String,
        val accountNum: String,
        var accountHolderInfoType: String = " ",
        var accountHolderInfo: String,
        var tranDtime: String,
        var payUserId: Long

    ) : ApiResponse()


    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
    data class RealNameInquiryResponseDTO(

        val apiTranId: String,
        val rspCode: String,
        val rspMessage: String,
        var apiTranDtm: String,
        var bankTranId: String?,
        var bankTranDate: String?,
        var bankCodeTran: String?,
        var bankRspCode: String?,
        var bankRspMessage: String?,
        var bankCodeStd: String?,
        var bankCodeSub: String?,
        var bankName: String?,
        var accountNum: String?,
        var accountHolderInfoType: String?,
        var accountHolderInfo: String?,
        var accountHolderName: String?,
        var accountType: String?,
        var savingsBankName: String?,
        var accountSeq: String?

    ) : ApiResponse()


    data class OpenApiDepositWalletDTO (
        val depositMoney: Int,
        val accountId: Long,
        val payUser: PayUser,
    )


}