package com.chatandpay.api.service

import com.chatandpay.api.common.SecurityUser
import com.chatandpay.api.repository.UserRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class SecurityUserDetailService(
    private val userRepository: UserRepository
) : UserDetailsService {

    @Cacheable("member", key = "#ulid")
    override fun loadUserByUsername(ulid: String): UserDetails {
        val member = userRepository.findByUlid(ulid)
        if (member != null) {
            return SecurityUser(member)
        } else {
            throw UsernameNotFoundException("해당 사용자가 없습니다.")
        }
    }
}