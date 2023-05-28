package com.chatandpay.api.exception

import com.chatandpay.api.code.ErrorCode
import com.chatandpay.api.common.ErrorResponse
import lombok.Getter
import lombok.RequiredArgsConstructor


@Getter
@RequiredArgsConstructor
class RestApiException() : RuntimeException() {
    private val errorEnum = ErrorCode.INTERNAL_SERVER_ERROR
    private val errorMsg = "Server error."
    val errorResponse = ErrorResponse(errorEnum.value, errorMsg)
}