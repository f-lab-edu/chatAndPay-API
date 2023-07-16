package com.chatandpay.api.common

import com.chatandpay.api.dto.ValidRefreshTokenResponse
import com.chatandpay.api.exception.RestApiException
import com.chatandpay.api.repository.UserRepository
import com.chatandpay.api.service.RedisService
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
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

    fun validateRefreshToken(accessToken: String, refreshToken: String): ValidRefreshTokenResponse {

        val findInfo: List<Any> = redisService.getStringValue(refreshToken)

        if(isInvalidated(refreshToken)) {
            throw RestApiException("유효한 토큰이 아닙니다.")
        }

        if(validateToken(accessToken)) {
            invalidateToken(refreshToken)
            throw RestApiException("accessToken 만료 전 요청 - refreshToken을 폐기합니다.")
        }

        val userPk = getUserPk(refreshToken)

        if (findInfo.size < 2) { throw RestApiException("유효한 토큰이 아닙니다.") }

        val firstElement = findInfo[0] as? String
        val firstElementLong = firstElement?.toLongOrNull() ?: throw RestApiException("유효한 토큰이 아닙니다.")

        if (userPk != firstElement) { throw RestApiException("유효한 토큰이 아닙니다.") }

        val userInfo = firstElementLong.toString()

        val findMember = userDetailsService.loadUserByUsername(userInfo)

        val role = findMember.authorities.stream()
                        .map { authority: GrantedAuthority? -> authority!!.authority }
                        .findFirst()
                        .orElseThrow{ RestApiException("토큰 발급 오류") }

        val newAccessToken = UserRole.findByRoleName(role)?.let { createAccessToken(firstElementLong, it) }

        return ValidRefreshTokenResponse(userInfo, newAccessToken)

    }

    fun createAccessToken(userPk: Long?, role: UserRole): String {

        val claims = Jwts.claims().setSubject(userPk.toString())
        claims["roles"] = role
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

    fun getAuthentication(token: String?): Authentication {
        val userDetails = userDetailsService.loadUserByUsername(getUserPk(token))
        return UsernamePasswordAuthenticationToken(userDetails, "", userDetails.authorities)
    }

    fun getUserPk(token: String?): String {
        return try {
            val subject = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).body.subject
            subject.toString()
        } catch (e: ExpiredJwtException) {
            e.printStackTrace()
            "Expired"
        } catch (e: JwtException) {
            e.printStackTrace()
            "Invalid"
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
            val claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken)
            return !claims.body.expiration.before(Date())
        } catch (e: ExpiredJwtException) {
            e.printStackTrace()
            false
        } catch (e: JwtException) {
            e.printStackTrace()
            false
        }
    }

    fun invalidateToken(refreshToken: String) {
        redisService.deleteStringValue(refreshToken)
    }

    fun isInvalidated(refreshToken: String) : Boolean {
        return redisService.getRequestTokenList(refreshToken).isEmpty()
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