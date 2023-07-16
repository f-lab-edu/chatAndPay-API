package com.chatandpay.api.domain

import com.fasterxml.jackson.annotation.JsonIgnore
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
    var money: Int,
    @OneToOne
    @JoinColumn(name = "pay_user_id")
    @JsonIgnore
    val payUser: PayUser,
    var version: Long? = 0,
) {
    override fun toString(): String {
        return "Wallet(id=$id, money=$money)"
    }
}