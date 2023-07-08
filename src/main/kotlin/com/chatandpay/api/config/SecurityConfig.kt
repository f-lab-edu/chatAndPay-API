package com.chatandpay.api.config

import com.chatandpay.api.common.JwtTokenProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter


@Configuration
@EnableWebSecurity
class SecurityConfig(
    private val jwtTokenProvider: JwtTokenProvider
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {

        http.csrf().disable()
            .authorizeRequests()
                .antMatchers("/users/token/**", "/users/login", "/users/auth/**", "/users/signup").permitAll()
                .anyRequest().authenticated()
                .and()
            .addFilterBefore(
                 JwtAuthenticationFilter(jwtTokenProvider),
                UsernamePasswordAuthenticationFilter::class.java
            )
            .formLogin().disable()

        http.headers()
                .frameOptions().sameOrigin()

        return http.build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder? {
        return BCryptPasswordEncoder()
    }

}