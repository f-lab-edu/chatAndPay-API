package com.chatandpay.api.common

import org.springframework.http.HttpStatus

abstract class ApiResponse {
    open val code: Int = HttpStatus.OK.value()

    fun getHttpStatus() : HttpStatus? {
        return when (code) {
            200 -> { HttpStatus.OK }
            400 -> { HttpStatus.BAD_REQUEST }
            else -> {
                enumValues<HttpStatus>().find { it.value() == code }
            }
        }
    }

}