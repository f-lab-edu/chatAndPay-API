package com.chatandpay.api.domain

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Field
import org.springframework.data.mongodb.core.mapping.FieldType
import java.time.LocalDateTime
import javax.persistence.Column

open class BaseDocumentEntity {

    @Id
    @Field("_id", targetType = FieldType.OBJECT_ID)
    var id: String? = null
        protected set

    @CreatedDate
    var createdAt: LocalDateTime = LocalDateTime.now()
        protected set

    @LastModifiedDate
    var updatedAt: LocalDateTime = LocalDateTime.now()
        protected set

}