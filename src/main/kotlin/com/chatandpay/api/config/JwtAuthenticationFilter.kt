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

            val httpRequest = request as HttpServletRequest
            val requestURI = httpRequest.requestURI

            if (isAuthenticated(requestURI)) {
                val token = jwtTokenProvider.resolveToken(httpRequest) ?: throw JwtException("토큰 포함 필요")
                if (jwtTokenProvider.validateToken(token)) {
                    val authentication = jwtTokenProvider.getAuthentication(token)
                    SecurityContextHolder.getContext().authentication = authentication
                }
                throw JwtException("토큰 재발급 필요")
            }

            chain.doFilter(request, response)
        } catch (ex: JwtException) {
            val message = ex.message ?: ""
            jwtExceptionHandler(response as HttpServletResponse, ErrorCode.BAD_REQUEST, message)
        }

    }

    private fun jwtExceptionHandler(response: HttpServletResponse, error: ErrorCode, message: String) {
        response.status = error.value
        response.contentType = "application/json"
        response.characterEncoding = "UTF-8"
        try {
            val json = ObjectMapper().writeValueAsString(ErrorResponse(error.value, message))
            response.writer.write(json)
        } catch (e: Exception) {
            logger.error("오류: $e")
        }
    }

    private fun isAuthenticated(requestURI: String): Boolean {
        val excludedPatterns = arrayOf("/login", "/auth", "/signup", "token")

        for (pattern in excludedPatterns) {
            if (requestURI.contains(pattern)) {
                return false
            }
        }
        return true
    }

}
