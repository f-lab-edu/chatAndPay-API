package com.chatandpay.api

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching

@SpringBootApplication
@EnableCaching
class ApiApplication

fun main(args: Array<String>) {
	runApplication<ApiApplication>(*args)
}