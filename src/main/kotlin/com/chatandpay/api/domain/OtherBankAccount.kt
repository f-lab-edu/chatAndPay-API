package com.chatandpay.api.domain

import javax.persistence.*

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["bankCode", "accountNumber"])])
data class OtherBankAccount(

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    val bankCode: String,
    val accountNumber: String,
    val accountName: String,
    val autoDebitAgree: String,
    @ManyToOne
    @JoinColumn(name = "pay_member_id")
    val payUser: PayUser,

)
