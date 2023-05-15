package com.chatandpay.api.domain

import lombok.AllArgsConstructor
import lombok.NoArgsConstructor
import javax.persistence.*

@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
data class Wallet(
    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    val money: Int,
    @OneToOne
    @JoinColumn(name = "pay_user_id")
    val payUser: PayUser
) {
    override fun toString(): String {
        return "Wallet(id=$id, money=$money)"
    }
}