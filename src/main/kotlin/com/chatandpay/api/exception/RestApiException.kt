package com.chatandpay.api.exception

import com.chatandpay.api.common.ErrorResponse
import lombok.Getter
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus


@Getter
@RequiredArgsConstructor
class RestApiException() : RuntimeException() {
    val errorResponse = ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Server error.")
}