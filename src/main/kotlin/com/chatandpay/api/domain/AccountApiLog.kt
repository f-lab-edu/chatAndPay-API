package com.chatandpay.api.domain

import javax.persistence.*

@Entity
@Table(name = "account_api_log")
class AccountApiLog(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var requester: Long,
    @Column(name = "account_id")
    var accountId: Long,
    @Column(name = "initial_amount")
    var initialAmount: Int,
    @Column(name = "final_amount")
    var finalAmount: Int,
    @Column(name = "attempt_cd")
    var attemptCd: Int

) : BaseCreatedEntity()
