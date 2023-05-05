package com.chatandpay.api.domain

import javax.persistence.*

@Entity
@Table(name = "member")
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

)

