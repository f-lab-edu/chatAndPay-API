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

    val bankCode: String,

    val accountNumber: String,

    val amount: Int,

    @Column(name = "transferred_yn")
    var transferred: Boolean


) : BaseEntity()
