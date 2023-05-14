package com.chatandpay.api.service

import com.chatandpay.api.domain.AccessToken
import com.chatandpay.api.repository.AccessTokenRepository
import com.chatandpay.api.service.dto.OpenApiAccessTokenDTO
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import java.net.URI
import java.time.LocalDateTime
import java.util.*
import javax.transaction.Transactional

@Service
class OpenApiService(val accessTokenRepository: AccessTokenRepository) {

    @Value("\${openapi.clientId}")
    val clientId: String? = null

    @Value("\${openapi.clientSecret}")
    val clientSecret: String? = null

    @Value("\${openapi.url}")
    val url: String? = null

    @Transactional
    fun getOpenApiAccessToken() : AccessToken? {
        val rest = RestTemplate()
        val uri = URI.create("$url/oauth/2.0/token")

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_FORM_URLENCODED
        headers.accept = Collections.singletonList(MediaType.APPLICATION_JSON)

        val param: MultiValueMap<String, String> = LinkedMultiValueMap()
        param.add("client_id", clientId)
        param.add("client_secret", clientSecret)
        param.add("scope", "oob")
        param.add("grant_type", "client_credentials")

        val now = LocalDateTime.now()
        val expiresInSeconds : Long = 7775999
        val expiredDate = now.minusSeconds(expiresInSeconds)

        var token = accessTokenRepository.findLatestAliveToken(expiredDate)

        if(token == null) {
            try {

                val res = rest.postForObject(
                    uri,
                    HttpEntity(param, headers),
                    OpenApiAccessTokenDTO::class.java
                    // TODO - 타 응답 반환 시 ResponceDTO 처리
                ) ?: throw RuntimeException("토큰 발급 실패")

                val accessToken = AccessToken(null, res.accessToken, res.tokenType, res.expiresIn, res.tokenType, res.clientUseCode, now, now)
                token = accessTokenRepository.save(accessToken)

            } catch (e: Exception) {
                e.printStackTrace()
                throw RuntimeException("토큰 발급 실패")
            }
        }

        return token
    }
}