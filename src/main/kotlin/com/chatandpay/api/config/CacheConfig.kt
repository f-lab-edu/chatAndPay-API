package com.chatandpay.api.config

import com.chatandpay.api.common.JwtTokenProvider.Companion.TOKEN_VALIDATION_SECOND
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import java.time.Duration

@EnableCaching
@Configuration
class CacheConfig {

    @Bean
    fun cacheManager(redisConnectionFactory: RedisConnectionFactory): CacheManager {
        val cacheConfigMap: Map<String, RedisCacheConfiguration> = mapOf(
            "member" to RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(TOKEN_VALIDATION_SECOND))
        )

        return RedisCacheManager.builder(redisConnectionFactory)
            .cacheDefaults(RedisCacheConfiguration.defaultCacheConfig())
            .withInitialCacheConfigurations(cacheConfigMap)
            .build()
    }

}
