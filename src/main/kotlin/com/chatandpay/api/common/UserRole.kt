package com.chatandpay.api.common

enum class UserRole(val roleName: String) {
    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    companion object {
        fun findByRoleName(roleName: String): UserRole? {
            // TODO ROLE_ 부분 조금 더 깔끔하게 처리 필요
            return values().find { it.roleName.split("_")[1] == roleName }
        }
    }

}