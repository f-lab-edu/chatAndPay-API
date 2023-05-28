package com.chatandpay.api.common

import lombok.Getter
import lombok.RequiredArgsConstructor

@Getter
@RequiredArgsConstructor
class ErrorResponse(
    override val code: Int,
    val errorMessage: String
) : ApiResponse()