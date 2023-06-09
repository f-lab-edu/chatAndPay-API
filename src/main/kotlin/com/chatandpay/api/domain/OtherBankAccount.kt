package com.chatandpay.api.domain

import com.fasterxml.jackson.annotation.JsonBackReference
import com.querydsl.core.annotations.QueryEntity
import javax.persistence.*

@Entity
@Table(uniqueConstraints = [UniqueConstraint(columnNames = ["bankCode", "accountNumber"])])
@QueryEntity
data class OtherBankAccount(

    @Id
    @Column(nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    val bankCode: String,
    val accountNumber: String,
    val accountName: String,
    val autoDebitAgree: String,
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "pay_member_id")
    val payUser: PayUser,

)
