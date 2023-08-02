package com.chatandpay.api.common

import com.chatandpay.api.dto.UserDTO
import com.chatandpay.api.exception.RestApiException
import com.chatandpay.api.service.RedisService
import io.jsonwebtoken.*
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.*
import java.util.Base64.*
import javax.annotation.PostConstruct
import javax.servlet.http.HttpServletRequest


@Component
class JwtTokenProvider (
    private val userDetailsService: UserDetailsService,
    private val redisService: RedisService
){

    @Value("spring.jwt.secret")
    private var secretKey: String? = null

    @PostConstruct
    protected fun init() {
        secretKey = getEncoder().encodeToString(secretKey!!.toByteArray())
    }

    fun regenerateToken(accessToken: String, refreshToken: String): UserDTO.ValidRefreshTokenResponse {

        val findInfo: List<Any> = redisService.getStringValue(refreshToken)

        if(isInvalidated(refreshToken)) {
            throw RestApiException("유효한 토큰이 아닙니다.")
        }

        if(validateToken(accessToken)) {
            invalidateToken(refreshToken, "refresh")
            throw RestApiException("accessToken 만료 전 요청 - refreshToken을 폐기합니다.")
        }

        val userPk = redisService.getStringValue(refreshToken)[0]

        if (findInfo.size < 2) { throw RestApiException("유효한 토큰이 아닙니다.") }

        val userInfo = findInfo[0] as? String
        val userInfoLong = userInfo?.toLongOrNull() ?: throw RestApiException("유효한 토큰이 아닙니다.")

        if (userPk != userInfo) { throw RestApiException("유효한 토큰이 아닙니다.") }

        val findMember = userDetailsService.loadUserByUsername(userInfo)

        val role = findMember.authorities.firstOrNull()?.authority ?: throw RestApiException("토큰 발급 오류")

        val newAccessToken  = UserRole.findByRoleName(role)?.let { createAccessToken(userInfoLong, it, findMember.username) }
        val newRefreshToken = createRefreshToken()

        invalidateToken(refreshToken, "refresh")

        return UserDTO.ValidRefreshTokenResponse(userInfo, newAccessToken, newRefreshToken)

    }

    fun createAccessToken(userPk: Long?, role: UserRole, cellphone: String): String {

        val claims = Jwts.claims().setSubject(userPk.toString())
        claims["roles"] = role
        claims["cellphone"] = cellphone
        val now = Date()

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now)
            .setExpiration(Date(now.time + TOKEN_VALIDATION_SECOND))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()

    }

    fun createRefreshToken(): String {

        val now = Date()
        return Jwts.builder()
            .setIssuedAt(now)
            .setExpiration(Date(now.time + REFRESH_TOKEN_VALIDATION_SECOND))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact()

    }

    fun getAuthentication(token: String): Authentication {
        val claims: Claims = getUserClaims(token) ?: throw JwtException("잘못된 토큰입니다.")

        claims["cellphone"] ?: throw JwtException("잘못된 토큰입니다.")

        val userDetails = userDetailsService.loadUserByUsername(claims.subject)

        if(userDetails.username != claims["cellphone"]) {
            throw JwtException("잘못된 토큰입니다.")
        }

        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    private fun getUserClaims(token: String?): Claims? {
        return try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body
        } catch (e: JwtException) {
            e.printStackTrace()
            null
        }
    }



    fun resolveToken(req: HttpServletRequest): String? {
        val authorizationHeader = req.getHeader("Authorization")
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7)
        }
        return null
    }

    fun validateToken(jwtToken: String?): Boolean {

        if(isLoggedOut(jwtToken) == true) return false

        return try {
            val claims = getUserClaims(jwtToken) ?: throw JwtException("잘못된 토큰입니다.")
            return !claims.expiration.before(Date())
        } catch (e: ExpiredJwtException) {
            e.printStackTrace()
            false
        } catch (e: JwtException) {
            e.printStackTrace()
            false
        }
    }

    fun invalidateToken(token: String, tokenType: String) {

        if(tokenType == "refresh") {
            redisService.deleteStringValue(token)
        }

        val data = ArrayList<String>()
        data.add(LocalDateTime.now().toString())
        val tokenExpirationTime = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body.expiration.time

        val currentTime = System.currentTimeMillis()
        val ttlExpirationTime = (tokenExpirationTime - currentTime) / 1000

        redisService.setStringValue(token, data, ttlExpirationTime)

    }

    fun isInvalidated(refreshToken: String) : Boolean {
        return redisService.getStringValue(refreshToken).isEmpty()
    }

    fun isLoggedOut(accessToken: String?): Boolean? {
        return if (accessToken == null) false else redisService.getStringValue(accessToken).isNotEmpty()
    }

    companion object {
        const val TOKEN_VALIDATION_SECOND = 1000L * 60 * 30 * 2
        const val REFRESH_TOKEN_VALIDATION_SECOND = 1000L * 120 * 1 * 2
        const val ACCESS_TOKEN_NAME = "accessToken"
        const val REFRESH_TOKEN_NAME = "refreshToken"
    }
}