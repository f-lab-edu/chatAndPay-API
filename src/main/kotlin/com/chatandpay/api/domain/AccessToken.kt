package com.chatandpay.api.domain

import lombok.AllArgsConstructor
import lombok.NoArgsConstructor
import javax.persistence.*

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "open_api_access_token")
data class AccessToken(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "access_token", nullable = false)
    var accessToken: String,

    @Column(name = "token_type", nullable = false)
    var tokenType: String,

    @Column(name = "expires_in", nullable = false)
    var expiresIn: String,

    @Column(name = "scope", nullable = false)
    var scope: String,

    @Column(name = "client_use_code", nullable = false)
    var clientUseCode: String,

): BaseEntity()