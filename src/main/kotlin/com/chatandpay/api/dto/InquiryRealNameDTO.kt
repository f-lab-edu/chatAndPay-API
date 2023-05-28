package com.chatandpay.api.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class InquiryRealNameDTO(


    var bankTranId: String,
    val bankCodeStd: String,
    val accountNum: String,
    var accountHolderInfoType: String = " ",
    var accountHolderInfo: String,
    var tranDtime: String,
    var payUserId: Long

)
