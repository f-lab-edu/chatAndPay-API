package com.chatandpay.api.common

import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import lombok.Getter
import lombok.RequiredArgsConstructor

@Getter
@RequiredArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class ErrorResponse(
    override val code: Int,
    val errorMessage: String
) : ApiResponse()