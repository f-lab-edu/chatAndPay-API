package com.chatandpay.api.domain.sms

import lombok.Getter
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor
import java.security.Timestamp

@Getter
@AllArgsConstructor
@NoArgsConstructor
class SmsResponse {
    private val statusCode: String? = null
    private val statusName: String? = null
    private val requestId: String? = null
    private val requestTime: Timestamp? = null
}