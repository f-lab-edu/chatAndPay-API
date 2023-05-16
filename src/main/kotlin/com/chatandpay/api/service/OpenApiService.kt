package com.chatandpay.api.service

import com.chatandpay.api.controller.dto.InquiryRealNameDTO
import com.chatandpay.api.domain.AccessToken
import com.chatandpay.api.repository.AccessTokenRepository
import com.chatandpay.api.repository.PayUserRepository
import com.chatandpay.api.service.dto.RealNameInquiryResponseDTO
import com.chatandpay.api.service.dto.OpenApiAccessTokenDTO
import com.chatandpay.api.utils.RandomUtil
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.*
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import java.net.URI
import java.nio.charset.Charset
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import javax.transaction.Transactional

@Service
class OpenApiService(val accessTokenRepository: AccessTokenRepository, val payUserRepository: PayUserRepository) {

    @Value("\${openapi.clientId}")
    val clientId: String? = null

    @Value("\${openapi.clientSecret}")
    val clientSecret: String? = null

    @Value("\${openapi.url}")
    val url: String? = null

    @Value("\${openapi.institutionCode}")
    val institutionCode: String? = null


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

    fun getInquiryRealName(inquiryDto : InquiryRealNameDTO) : RealNameInquiryResponseDTO {

        val rest = RestTemplate()
        val uri = URI.create("$url/v2.0/inquiry/real_name")

        val token = getOpenApiAccessToken()?.accessToken

        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer$token")
        headers.contentType = MediaType.APPLICATION_JSON
        headers.acceptCharset = listOf(Charset.forName("UTF-8"))

        inquiryDto.bankTranId =  institutionCode + "U" + RandomUtil.generateRandomNineDigits()
        inquiryDto.accountHolderInfo = payUserRepository.findById(inquiryDto.payUserId)?.birthDate?.substring(2,8) ?: throw IllegalAccessException("회원 조회 실패")
        inquiryDto.tranDtime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")).toString()


        val objectMapper = ObjectMapper().apply {
            propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
            // TODO - SNAKE_CASE 변경 필요
        }
        val jsonBody = objectMapper.writeValueAsString(inquiryDto)

        return rest.postForObject(
            uri,
            HttpEntity(jsonBody, headers),
            RealNameInquiryResponseDTO::class.java
            // TODO - 타 응답 반환 시 ResponceDTO 처리
        ) ?: throw RuntimeException("조회 실패")
    }

    fun withdrawMoney(chargeMoney: Int) : Int {

        val rest = RestTemplate()
        val uri = URI.create("$url/v2.0/transfer/withdraw/acnt_num")

        // TODO token 종류 상이함으로 인한 임시 서버 설정 필요

        return chargeMoney
    }

}