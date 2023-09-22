package com.chatandpay.api.service

import com.chatandpay.api.code.BankRspCode
import com.chatandpay.api.exception.WalletChargeAttemptException
import com.chatandpay.api.domain.AccessToken
import com.chatandpay.api.dto.*
import com.chatandpay.api.exception.RestApiException
import com.chatandpay.api.repository.AccessTokenRepository
import com.chatandpay.api.repository.PayUserRepository
import com.chatandpay.api.utils.RandomUtil
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
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

    @Value("\${openapi.client-id}")
    val clientId: String? = null

    @Value("\${openapi.client-secret}")
    val clientSecret: String? = null

    @Value("\${openapi.url}")
    val url: String? = null

    @Value("\${openapi.institution-code}")
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
                    OpenApiDTO.OpenApiAccessTokenDTO::class.java
                    // TODO - 타 응답 반환 시 ResponceDTO 처리
                ) ?: throw RestApiException("토큰 발급 실패")

                val accessToken = AccessToken(null, res.accessToken, res.tokenType, res.expiresIn, res.tokenType, res.clientUseCode)
                token = accessTokenRepository.save(accessToken)

            } catch (e: Exception) {
                e.printStackTrace()
                throw RestApiException("토큰 발급 실패")
            }
        }

        return token
    }

    fun getInquiryRealName(inquiryDto : OpenApiDTO.RealNameInquiryRequestDTO) : OpenApiDTO.RealNameInquiryResponseDTO {

        val rest = RestTemplate()
        val uri = URI.create("$url/v2.0/inquiry/real_name")

        val token = getOpenApiAccessToken()?.accessToken

        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer$token")
        headers.contentType = MediaType.APPLICATION_JSON
        headers.acceptCharset = listOf(Charset.forName("UTF-8"))

        // 은행거래고유번호(bankTranId): 이용기관코드 + 생성주체구분코드(U) + 이용기관 부여번호(9자리, 1일 내 중복 없이 랜덤 부여 필요)
        inquiryDto.bankTranId =  institutionCode + "U" + RandomUtil.generateRandomNineDigits()
        inquiryDto.accountHolderInfo = payUserRepository.findById(inquiryDto.payUserId)?.birthDate?.substring(2,8) ?: throw IllegalAccessException("회원 조회 실패")
        inquiryDto.tranDtime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")).toString()


        val objectMapper = ObjectMapper().apply {
            propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
            // TODO - SNAKE_CASE 변경 필요
        }
        val jsonBody = objectMapper.writeValueAsString(inquiryDto)

        val returnRestTemplate = rest.postForObject(
            uri,
            HttpEntity(jsonBody, headers),
            OpenApiDTO.RealNameInquiryResponseDTO::class.java
        ) ?: throw RestApiException("조회 실패")

        returnRestTemplate.bankRspCode?.let { checkBankRspCode(it) }

        return returnRestTemplate

    }

    fun withdrawMoney(dto: OpenApiDTO.OpenApiDepositWalletDTO) : OpenApiDTO.WithdrawMoneyResponseDTO {

        val rest = RestTemplate()
        val uri = URI.create("$url/v2.0/transfer/withdraw/acnt_num")

        val token = getOpenApiAccessToken()?.accessToken

        val headers = HttpHeaders()
        headers.set("Authorization", "Bearer$token")
        headers.contentType = MediaType.APPLICATION_JSON
        headers.acceptCharset = listOf(Charset.forName("UTF-8"))

        val objectMapper = ObjectMapper().apply {
            propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE
            registerModule(JavaTimeModule())
        }

        val jsonBody = objectMapper.writeValueAsString(dto)

        val returnRestTemplate = rest.postForObject(
            uri,
            HttpEntity(jsonBody, headers),
            OpenApiDTO.WithdrawMoneyResponseDTO::class.java
        ) ?: throw RestApiException("조회 실패")

        checkBankRspCode(returnRestTemplate.bankRspCode, "W", dto)

        return returnRestTemplate
    }

    fun checkBankRspCode(bankRspCode: String, type: String? = null, dto: Any? = null) {
        if (bankRspCode != "000") {
            // bankRspCode, 응답코드(참가기관)이 정상(000)이 아닌 경우에는 ‘이체 불능’으로 간주

            val rspCodeCtnt = BankRspCode.values().find { it.bankRspCode == bankRspCode }
                ?: when (type) {
                    "W" -> throw WalletChargeAttemptException ("$bankRspCode: 존재하지 않는 참가기관 응답코드입니다.", dto as OpenApiDTO.OpenApiDepositWalletDTO)
                    else -> throw IllegalArgumentException("$bankRspCode: 존재하지 않는 참가기관 응답코드입니다.")
                }

            when (type) {
                "W" -> throw WalletChargeAttemptException ("[대외계 에러]: ${rspCodeCtnt.msg}", dto as OpenApiDTO.OpenApiDepositWalletDTO)
                else -> throw RestApiException("[대외계 에러]: ${rspCodeCtnt.msg}")
            }

        }
    }
}