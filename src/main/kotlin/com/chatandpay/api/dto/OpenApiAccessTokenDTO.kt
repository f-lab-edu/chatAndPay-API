package com.chatandpay.api.dto

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class OpenApiAccessTokenDTO (
    val accessToken: String,
    val tokenType: String,
    val expiresIn: String,
    val scope: String,
    val clientUseCode: String,
)