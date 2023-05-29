package com.chatandpay.api.controller

import com.chatandpay.api.code.ErrorCode
import com.chatandpay.api.common.ApiResponse
import com.chatandpay.api.common.ErrorResponse
import com.chatandpay.api.common.SuccessResponse
import com.chatandpay.api.dto.InquiryRealNameDTO
import com.chatandpay.api.dto.RealNameInquiryResponseDTO
import com.chatandpay.api.service.OpenApiService
import lombok.RequiredArgsConstructor
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/openapi")
@RequiredArgsConstructor
class OpenApiController (val openApiService : OpenApiService) {

    @GetMapping("/token")
    fun getOpenApiToken(): ResponseEntity<ApiResponse> {

        val tokenIssued = openApiService.getOpenApiAccessToken()

        return if (tokenIssued != null) {
            val successResponse = SuccessResponse("토큰 발급 완료")
            ResponseEntity.ok(successResponse)
        } else {
            val errorCode = ErrorCode.INTERNAL_SERVER_ERROR
            val errorResponse = ErrorResponse(errorCode.value, "토큰 발급 실패")
            ResponseEntity.badRequest().body(errorResponse)
        }

    }

    @PostMapping("/real_name")
    fun getAccountRealName(@RequestBody inquiryDto: InquiryRealNameDTO): ResponseEntity<RealNameInquiryResponseDTO> {

        val response = openApiService.getInquiryRealName(inquiryDto)
        return ResponseEntity.ok(response)

    }

}