package com.chatandpay.api.dto

import com.fasterxml.jackson.annotation.JsonProperty
data class RealNameInquiryResponseDTO(

    @JsonProperty("api_tran_id")
    val apiTranId: String,

    @JsonProperty("rsp_code")
    val rspCode: String,

    @JsonProperty("rsp_message")
    val rspMessage: String,

    @JsonProperty("api_tran_dtm")
    var apiTranDtm: String,

    @JsonProperty("bank_tran_id")
    var bankTranId: String?,

    @JsonProperty("bank_tran_date")
    var bankTranDate: String?,

    @JsonProperty("bank_code_tran")
    var bankCodeTran: String?,

    @JsonProperty("bank_rsp_code")
    var bankRspCode: String?,

    @JsonProperty("bank_rsp_message")
    var bankRspMessage: String?,

    @JsonProperty("bank_code_std")
    var bankCodeStd: String?,

    @JsonProperty("bank_code_sub")
    var bankCodeSub: String?,

    @JsonProperty("bank_name")
    var bankName: String?,

    @JsonProperty("account_num")
    var accountNum: String?,

    @JsonProperty("account_holder_info_type")
    var accountHolderInfoType: String?,

    @JsonProperty("account_holder_info")
    var accountHolderInfo: String?,

    @JsonProperty("account_holder_name")
    var accountHolderName: String?,

    @JsonProperty("account_type")
    var accountType: String?,

    @JsonProperty("savings_bank_name")
    var savingsBankName: String?,

    @JsonProperty("account_seq")
    var accountSeq: String?

)
