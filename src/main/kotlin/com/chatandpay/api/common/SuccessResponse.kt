package com.chatandpay.api.common

import lombok.Getter
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus

@Getter
@RequiredArgsConstructor
class SuccessResponse(
    override val status: HttpStatus,
    val successMessage: String
) : ApiResponse()