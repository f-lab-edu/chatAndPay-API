package com.chatandpay.api.common

import org.springframework.http.HttpStatus

abstract class ApiResponse {
    open val status: HttpStatus = HttpStatus.OK
}