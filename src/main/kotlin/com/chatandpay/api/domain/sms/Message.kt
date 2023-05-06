package com.chatandpay.api.domain.sms

import lombok.*

@Data
@AllArgsConstructor
@NoArgsConstructor
data class Message(
    val to: String,
    val subject: String?,
    val content: String,
)