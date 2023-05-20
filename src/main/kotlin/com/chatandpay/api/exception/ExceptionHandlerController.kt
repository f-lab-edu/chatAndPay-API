package com.chatandpay.api.exception

import com.chatandpay.api.common.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import javax.persistence.EntityNotFoundException


@RestControllerAdvice
internal class ExceptionHandlerController {

    @ExceptionHandler(IllegalAccessException::class)
    protected fun handleIllegalAccessException(ex: IllegalAccessException): ResponseEntity<ErrorResponse>? {
        val errorCode =  ErrorResponse(HttpStatus.BAD_REQUEST, ex.message ?: "Invalid request.")
        return handleExceptionInternal(errorCode)
    }

    @ExceptionHandler(EntityNotFoundException::class)
    protected fun handleEntityNotFoundException(ex: EntityNotFoundException): ResponseEntity<ErrorResponse>? {
        val errorCode =  ErrorResponse(HttpStatus.BAD_REQUEST, ex.message ?: "Invalid request.")
        return handleExceptionInternal(errorCode)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    protected fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<ErrorResponse>? {
        val errorCode =  ErrorResponse(HttpStatus.BAD_REQUEST, ex.message ?: "Invalid request.")
        return handleExceptionInternal(errorCode)
    }

    @ExceptionHandler(RestApiException::class)
    protected fun handleRestApiException(ex: RestApiException): ResponseEntity<ErrorResponse>? {
        return handleExceptionInternal(ex.errorResponse)
    }

    private fun handleExceptionInternal(errorResponse : ErrorResponse): ResponseEntity<ErrorResponse> {
        return ResponseEntity.status(errorResponse.status).body(errorResponse)
    }
}