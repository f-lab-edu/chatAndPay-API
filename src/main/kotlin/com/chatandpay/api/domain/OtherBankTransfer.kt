package com.chatandpay.api.domain

import lombok.AllArgsConstructor
import lombok.NoArgsConstructor
import java.util.*
import javax.persistence.*

@Entity
@AllArgsConstructor
@NoArgsConstructor
data class OtherBankTransfer(

    @Id
    @Column(columnDefinition = "BINARY(16)")
    val uuid: UUID,

    @OneToOne
    @JoinColumn(name = "sender_id")
    val sender: PayUser,

    @OneToOne
    @JoinColumn(name = "receiver_account")
    val receiverAccount: OtherBankAccount,

    val amount: Int,

    @Column(name = "transferred_yn")
    var transferred: Boolean,


    var transferType: String = "O"


) : BaseEntity()
