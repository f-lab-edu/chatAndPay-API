package com.chatandpay.api

import com.chatandpay.api.domain.User
import com.chatandpay.api.domain.sms.Message
import com.chatandpay.api.service.SmsService
import com.chatandpay.api.service.UserService
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
class ApiApplicationTests {

	@Autowired
	private val smsService: SmsService? = null

	@Autowired
	private val userService: UserService? = null

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
		val message = Message("01072808790", null, "메시지 전송 테스트")
		smsService!!.sendSms(message)
	}
	@Test
	fun authSendSms() {
		smsService!!.authSendSms("01072808790")
	}

	@Test
	fun authLoginConfirm() {
		userService!!.login(User(userId = "id", name="", cellphone = "", password = "password", verificationId = 0))
	}

	@Test
	fun contextLoads() {
	}

}
