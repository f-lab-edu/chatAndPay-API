package com.chatandpay.api.dto

import com.chatandpay.api.common.ApiResponse
import lombok.NoArgsConstructor
import lombok.ToString


@ToString
@NoArgsConstructor
data class TokenInfo(
    val accessToken: String,
    val refreshToken: String,
): ApiResponse()