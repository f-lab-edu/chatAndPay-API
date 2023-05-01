package com.chatandpay.api.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain


@Configuration
@EnableWebSecurity
class SecurityConfig {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {

        http.csrf().disable()
            .authorizeRequests()
                .anyRequest().permitAll()
                .and()
            .formLogin().disable()

        http.headers()
                .frameOptions().sameOrigin()

        return http.build()
    }

}