package com.chatandpay.api.domain

import lombok.AllArgsConstructor
import lombok.NoArgsConstructor
import javax.persistence.*

@Entity
@Table(name = "member")
@AllArgsConstructor
@NoArgsConstructor
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
    var password: String? = null,
    @Column(nullable = false)
    var name: String,
    @Column(nullable = false)
    var verificationId: Long,

)

