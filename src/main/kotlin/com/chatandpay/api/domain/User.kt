package com.chatandpay.api.domain

import com.chatandpay.api.common.UserRole
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor
import javax.persistence.*
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.io.Serializable

@Entity
@Table(name = "member")
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class User (

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column(nullable = false)
    var cellphone: String,
    @Column(nullable = true)
    var userId: String? = null,
    @Column(nullable = true)
    var email: String? = null,
    @Column(nullable = true)
    var password: String? = null,
    @Column(nullable = false)
    var name: String,
    @Column(nullable = false)
    var verificationId: Long,
    @Enumerated(EnumType.STRING)
    var role: UserRole,
    @Column(nullable = false)
    var ulid: String,

): Serializable, BaseEntity()

