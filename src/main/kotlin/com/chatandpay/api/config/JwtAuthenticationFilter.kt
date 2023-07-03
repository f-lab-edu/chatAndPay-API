package com.chatandpay.api.config

import com.chatandpay.api.code.ErrorCode
import com.chatandpay.api.common.ErrorResponse
import com.chatandpay.api.common.JwtTokenProvider
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.JwtException
import lombok.RequiredArgsConstructor
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@RequiredArgsConstructor
class JwtAuthenticationFilter(
    private val jwtTokenProvider: JwtTokenProvider
) : GenericFilterBean() {

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {

        try {
            val token = jwtTokenProvider.resolveToken((request as HttpServletRequest))

            if (token != null && jwtTokenProvider.validateToken(token)) {
                val authentication = jwtTokenProvider.getAuthentication(token)
                SecurityContextHolder.getContext().authentication = authentication
            }

            chain.doFilter(request, response)
        } catch (ex: JwtException) {
            jwtExceptionHandler(response as HttpServletResponse, ErrorCode.BAD_REQUEST)
        }

    }

    private fun jwtExceptionHandler(response: HttpServletResponse, error: ErrorCode) {
        response.status = error.value
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        try {
            val json = ObjectMapper().writeValueAsString(ErrorResponse(error.value, "유효한 토큰이 아닙니다."))
            response.writer.write(json)
        } catch (e: Exception) {
            logger.error("오류: $e")
        }
    }

}
