package com.chatandpay.api.service.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class OpenApiAccessTokenDTO (
    @JsonProperty("access_token") val accessToken: String,
    @JsonProperty("token_type") val tokenType: String,
    @JsonProperty("expires_in") val expiresIn: String,
    val scope: String,
    @JsonProperty("client_use_code") val clientUseCode: String,
)