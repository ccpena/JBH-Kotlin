package com.kkpa.jbh.domain

import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.EntityListeners
import javax.persistence.MappedSuperclass

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class AuditModel {

    @Column(name = "created_at", nullable = false, updatable = false)
    open val createdAt = LocalDateTime.now()

    @Column(name = "updated_at", nullable = true, updatable = true, insertable = false)
    open val updatedAt = LocalDateTime.now()
}
