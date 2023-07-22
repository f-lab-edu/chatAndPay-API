package com.chatandpay.api.exception

import com.chatandpay.api.code.ErrorCode
import com.chatandpay.api.common.ErrorResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.concurrent.TimeoutException
import javax.persistence.EntityNotFoundException


@RestControllerAdvice
internal class ExceptionHandlerController {

    @ExceptionHandler(IllegalAccessException::class)
    protected fun handleIllegalAccessException(ex: IllegalAccessException): ResponseEntity<ErrorResponse>? {
        val errorEnum = ErrorCode.BAD_REQUEST
        val errorMsg = ex.message ?: "Invalid request."
        val errorCode = ErrorResponse(errorEnum.value, errorMsg)
        return handleExceptionInternal(errorCode)
    }

    @ExceptionHandler(EntityNotFoundException::class)
    protected fun handleEntityNotFoundException(ex: EntityNotFoundException): ResponseEntity<ErrorResponse>? {
        val errorEnum = ErrorCode.BAD_REQUEST
        val errorMsg = ex.message ?: "Invalid request."
        val errorCode = ErrorResponse(errorEnum.value, errorMsg)
        return handleExceptionInternal(errorCode)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    protected fun handleIllegalArgumentException(ex: IllegalArgumentException): ResponseEntity<ErrorResponse>? {
        val errorEnum = ErrorCode.BAD_REQUEST
        val errorMsg = ex.message ?: "Invalid request."
        val errorCode = ErrorResponse(errorEnum.value, errorMsg)
        return handleExceptionInternal(errorCode)
    }

    @ExceptionHandler(TimeoutException::class)
    protected fun handleTimeoutException(ex: TimeoutException): ResponseEntity<ErrorResponse>? {
        val errorEnum = ErrorCode.REQUEST_TIMEOUT
        val errorMsg = ex.message ?: "Request Timeout."
        val errorCode = ErrorResponse(errorEnum.value, errorMsg)
        return handleExceptionInternal(errorCode)
    }

    @ExceptionHandler(WalletChargeAttemptException::class)
    protected fun handleWalletChargeAttemptException(ex: WalletChargeAttemptException): ResponseEntity<ErrorResponse>? {
        val errorEnum = if (ex.message.contains("대외계")) ErrorCode.INTERNAL_SERVER_ERROR else ErrorCode.BAD_REQUEST
        val errorMsg = ex.message
        val errorCode = ErrorResponse(errorEnum.value, errorMsg)
        return handleExceptionInternal(errorCode)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    protected fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse>? {
        val errorEnum = ErrorCode.BAD_REQUEST
        val errorMsg = methodArgumentNotValidErrorParser(ex)
        val errorCode = ErrorResponse(errorEnum.value, errorMsg)
        return handleExceptionInternal(errorCode)
    }


    @ExceptionHandler(RestApiException::class, Exception::class)
    protected fun handleRestApiException(ex: RestApiException): ResponseEntity<ErrorResponse>? {
        return handleExceptionInternal(ex.errorResponse)
    }

    private fun handleExceptionInternal(errorResponse : ErrorResponse): ResponseEntity<ErrorResponse> {
        val httpStatus = errorResponse.getHttpStatus() ?: HttpStatus.INTERNAL_SERVER_ERROR
        return ResponseEntity.status(httpStatus).body(errorResponse)
    }

    private fun methodArgumentNotValidErrorParser(ex: MethodArgumentNotValidException) : String {
        val errors = ex.bindingResult.fieldErrors.mapNotNull{ "${it.field}: ${it.defaultMessage}"}
        return errors.joinToString(", ")
    }

}