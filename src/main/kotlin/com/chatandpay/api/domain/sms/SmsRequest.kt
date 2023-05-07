package com.chatandpay.api.domain.sms

import lombok.*
@Data
@AllArgsConstructor
@NoArgsConstructor
data class SmsRequest(
    val type: String,
    val contentType: String?,
    val countryCode: String?,
    val from: String,
    val content: String,
    val messages: List<Message>,
)

