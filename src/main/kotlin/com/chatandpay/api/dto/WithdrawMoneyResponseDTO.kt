package com.chatandpay.api.dto
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class WithdrawMoneyResponseDTO(
    val apiTranId: String,
    val apiTranDtm: String,
    val rspCode: String,
    val rspMessage: String,
    val dpsBankCodeStd: String,
    val dpsBankCodeSub: String,
    val dpsBankName: String,
    val dpsAccountNumMasked: String,
    val dpsPrintContent: String,
    val dpsAccountHolderName: String,
    val bankTranId: String,
    val bankTranDate: String,
    val bankCodeTran: String,
    val bankRspCode: String,
    val bankRspMessage: String,
    val fintechUseNum: String,
    val accountAlias: String,
    val bankCodeStd: String,
    val bankCodeSub: String,
    val bankName: String,
    val savingsBankName: String?,
    val accountNumMasked: String,
    val printContent: String,
    val accountHolderName: String,
    val tranAmt: String,
    val wdLimitRemainAmt: String
)
