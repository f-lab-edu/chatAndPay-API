package com.chatandpay.api.domain

import com.chatandpay.api.common.LogType
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "logs")
data class Log(
    val className: String,
    val methodName: String,
    val logType: LogType,
    val argumentValue: String? = null,
    val resultValue: String? = null,
) : BaseDocumentCreatedEntity()
