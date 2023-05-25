package com.chatandpay.api.code

enum class ErrorCode(val value: Int) {
    OK(200),
    CREATED(201),
    BAD_REQUEST(400),
    FORBIDDEN(403),
    NOT_FOUND(404),
    REQUEST_TIMEOUT(408),
    INTERNAL_SERVER_ERROR(500);
}