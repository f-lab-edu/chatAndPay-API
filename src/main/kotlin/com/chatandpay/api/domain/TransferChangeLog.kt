package com.chatandpay.api.domain

import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document(collection = "transfer_change_logs")
data class TransferChangeLog(
    val beforeUuid: UUID,
    val afterUuid: UUID,
    val changeType: String,
    val isSucceed: Boolean,
    var beforeSender: Long? = null,
    var afterSender: Long? = null,
    var beforeReceiver: Long? = null,
    var afterReceiver: Long? = null,
) : BaseDocumentCreatedEntity()