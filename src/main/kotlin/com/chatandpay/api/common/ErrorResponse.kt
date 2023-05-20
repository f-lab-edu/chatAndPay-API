package com.chatandpay.api.common

import lombok.Getter
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus

@Getter
@RequiredArgsConstructor
class ErrorResponse(
    override val status: HttpStatus,
    val errorMessage: String
) : ApiResponse()