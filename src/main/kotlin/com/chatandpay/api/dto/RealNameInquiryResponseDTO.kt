package com.chatandpay.api.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

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

)
