package com.chatandpay.api.service

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit
import javax.annotation.Resource


@Service
class RedisService (
    @Resource(name = "redisTemplate")
    private val redisTemplate: RedisTemplate<String, String>
) {

    fun setStringValue(key: String, values: List<String>, expirationSeconds: Long) {
        val valueArray = values.toTypedArray()
        redisTemplate.opsForList().rightPushAll(key, *valueArray)
        redisTemplate.expire(key, expirationSeconds, TimeUnit.SECONDS)
    }

    fun deleteStringValue(key: String) {
        redisTemplate.delete(key)
    }


    fun getStringValue(key: String): List<String> {
        return redisTemplate.opsForList().range(key, 0, -1) ?: emptyList()
    }


    fun getRequestTokenList(refreshToken: String): List<String> {
        val tokenList = redisTemplate.opsForList().range(refreshToken, 0, -1)
        return tokenList ?: emptyList()
    }


}