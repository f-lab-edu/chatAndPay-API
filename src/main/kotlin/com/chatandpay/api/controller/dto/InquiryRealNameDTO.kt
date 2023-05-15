package com.chatandpay.api.controller.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class InquiryRealNameDTO(

    @JsonProperty("bank_tran_id")
    var bankTranId: String,

    @JsonProperty("bank_code_std")
    val bankCodeStd: String,

    @JsonProperty("account_num")
    val accountNum: String,

    @JsonProperty("account_holder_info_type")
    var accountHolderInfoType: String = " ",

    @JsonProperty("account_holder_info")
    var accountHolderInfo: String,

    @JsonProperty("tran_dtime")
    var tranDtime: String,

    @JsonProperty("pay_user_id")
    var payUserId: Long

)
