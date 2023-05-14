package com.chatandpay.api.controller

import com.chatandpay.api.service.OpenApiService
import lombok.RequiredArgsConstructor
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/openapi")
@RequiredArgsConstructor
class OpenApiController (val openApiService : OpenApiService) {

    @GetMapping("/token")
    fun getOpenApiToken(): ResponseEntity<Any> {

        try {
            openApiService.getOpenApiAccessToken()
        } catch(e: Exception) {
            return ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
        }

        return ResponseEntity.ok("ok")
    }


}