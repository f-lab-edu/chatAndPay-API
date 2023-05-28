package com.chatandpay.api.domain

import lombok.Getter
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@Getter
@EntityListeners(AuditingEntityListener::class)
@MappedSuperclass
open class BaseCreatedEntity {
    @CreatedDate
    @Column(updatable = false)
    var createdAt: LocalDateTime? = null
        protected set
    
}