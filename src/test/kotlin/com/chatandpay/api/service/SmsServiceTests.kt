package com.chatandpay.api.service

import com.chatandpay.api.domain.sms.Message
import com.fasterxml.jackson.core.JsonProcessingException
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.io.UnsupportedEncodingException
import java.net.URISyntaxException
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.text.ParseException


@SpringBootTest
class SmsServiceTests {

	@Autowired
	private val smsService: SmsService? = null

	@Test
	@Throws(
		JsonProcessingException::class,
		ParseException::class,
		UnsupportedEncodingException::class,
		URISyntaxException::class,
		NoSuchAlgorithmException::class,
		InvalidKeyException::class
	)
	fun sendSms() {
		val message = Message("01012341234", null, "메시지 전송 테스트")
		smsService!!.sendSms(message)
	}
	@Test
	fun authSendSms() {
		smsService!!.authSendSms("01012341234")
	}


}
