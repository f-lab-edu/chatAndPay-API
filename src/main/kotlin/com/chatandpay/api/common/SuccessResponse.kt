package com.chatandpay.api.common

import lombok.Getter
import lombok.RequiredArgsConstructor

@Getter
@RequiredArgsConstructor
class SuccessResponse(
    val successMessage: String
) : ApiResponse()