package com.chatandpay.api.common

import com.chatandpay.api.domain.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.userdetails.UserDetails
import java.io.Serializable

class SecurityUser(user: User) : UserDetails, Serializable {
    private val user: User

    init {
        this.user = user
    }

    override fun getAuthorities(): MutableList<GrantedAuthority> = AuthorityUtils.createAuthorityList(user.role.toString())

    override fun getPassword() = user.password

    override fun getUsername() = user.cellphone

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true

    fun getMember(): User {
        return user
    }

}
