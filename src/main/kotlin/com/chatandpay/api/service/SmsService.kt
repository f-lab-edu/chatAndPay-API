package com.chatandpay.api.service

import com.chatandpay.api.domain.sms.Message
import com.chatandpay.api.domain.sms.SmsRequest
import com.chatandpay.api.domain.sms.SmsResponse
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import java.io.UnsupportedEncodingException
import java.net.URI
import java.net.URISyntaxException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


@Service
class SmsService {

    @Value("\${sens.accessKey}")
    private val accessKey: String? = null

    @Value("\${sens.secretKey}")
    private val secretKey: String? = null

    @Value("\${sens.serviceId}")
    private val serviceId: String? = null

    @Value("\${sens.senderPhone}")
    private val phone: String? = null


    @Throws(
        JsonProcessingException::class,
        RestClientException::class,
        URISyntaxException::class,
        InvalidKeyException::class,
        NoSuchAlgorithmException::class,
        UnsupportedEncodingException::class
    )
    fun sendSms(message: Message): SmsResponse? {

        val time = System.currentTimeMillis()

        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        headers.set("x-ncp-apigw-timestamp", time.toString())
        headers.set("x-ncp-iam-access-key", accessKey)
        headers.set("x-ncp-apigw-signature-v2", makeSignature(time))

        val messages: MutableList<Message> = ArrayList()
        messages.add(message)

        val request = phone?.let {
            SmsRequest(
                type = "SMS",
                contentType = "COMM",
                countryCode = "82",
                from = it,
                content = message.content,
                messages = messages,
            )
        }

        val objectMapper = ObjectMapper()
        val body = objectMapper.writeValueAsString(request)
        val httpBody: HttpEntity<String> = HttpEntity(body, headers)

        val restTemplate = RestTemplate()
        restTemplate.requestFactory = HttpComponentsClientHttpRequestFactory()

        return restTemplate.postForObject(
            URI("https://sens.apigw.ntruss.com/sms/v2/services/$serviceId/messages"), httpBody,
            SmsResponse::class.java
        )
    }


    @Throws(NoSuchAlgorithmException::class, UnsupportedEncodingException::class, InvalidKeyException::class)
    fun makeSignature(time: Long): String {
        val space = " "
        val newLine = "\n"
        val method = "POST"
        val url = "/sms/v2/services/" + this.serviceId + "/messages"
        val timestamp = time.toString()
        val accessKey = this.accessKey
        val secretKey = this.secretKey

        val message = StringBuilder()
            .append(method)
            .append(space)
            .append(url)
            .append(newLine)
            .append(timestamp)
            .append(newLine)
            .append(accessKey)
            .toString()

        val signingKey = SecretKeySpec(secretKey!!.toByteArray(charset("UTF-8")), "HmacSHA256")
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(signingKey)

        val rawHmac = mac.doFinal(message.toByteArray(charset("UTF-8")))

        return Base64.getEncoder().encodeToString(rawHmac)
    }

}