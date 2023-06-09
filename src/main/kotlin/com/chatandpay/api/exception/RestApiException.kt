package com.chatandpay.api.exception

import com.chatandpay.api.code.ErrorCode
import com.chatandpay.api.common.ErrorResponse
import lombok.Getter
import lombok.RequiredArgsConstructor


@Getter
@RequiredArgsConstructor
class RestApiException(message : String? = null, errorCd: ErrorCode? = null) : RuntimeException() {
    private val errorEnum = errorCd ?: ErrorCode.INTERNAL_SERVER_ERROR
    private val errorMsg = message ?: "Server error."
    override val message: String
        get() = errorMsg

    val errorResponse = ErrorResponse(errorEnum.value, errorMsg)
}