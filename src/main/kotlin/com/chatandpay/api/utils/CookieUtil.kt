package com.chatandpay.api.utils

import com.chatandpay.api.common.JwtTokenProvider
import org.springframework.stereotype.Component
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse

@Component
class CookieUtil {
    companion object {
        fun addTokenCookies(accessToken: String?, refreshToken: String?, response: HttpServletResponse) {
            val accessTokenCookie = Cookie(JwtTokenProvider.ACCESS_TOKEN_NAME, accessToken)
            val refreshTokenCookie = Cookie(JwtTokenProvider.REFRESH_TOKEN_NAME, refreshToken)

            accessTokenCookie.isHttpOnly = true
            refreshTokenCookie.isHttpOnly = true

            response.addCookie(accessTokenCookie)
            response.addCookie(refreshTokenCookie)
        }
    }
}
