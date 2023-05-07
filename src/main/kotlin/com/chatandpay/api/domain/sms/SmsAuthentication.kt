package com.chatandpay.api.domain.sms

import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "sms_auth")
data class SmsAuthentication (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "auth_number")
    val authNumber: String,

    @Column(name = "phone_number")
    val phoneNumber: String,

    @Column(name = "request_time")
    val requestTime: LocalDateTime,

    @Column(name = "confirm_time")
    var confirmTime: LocalDateTime? = null,

    @Column(name = "is_verified")
    var isVerified: Boolean = false
)
