package com.chatandpay.api.dto

import com.chatandpay.api.common.ApiResponse
import lombok.Getter
import lombok.ToString


@Getter
@ToString
class ValidRefreshTokenResponse(
    val userPk: String?,
    val accessToken: String?
): ApiResponse()