package com.kkpa.jbh.domain.Users

import com.kkpa.jbh.domain.AuditModel
import com.kkpa.jbh.domain.DTOConverter
import com.kkpa.jbh.dto.UserDTO
import com.kkpa.jbh.util.DefaultValues
import java.util.UUID
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.JoinTable
import javax.persistence.ManyToMany
import javax.persistence.Table
import javax.persistence.UniqueConstraint
import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
/**
 * This constraints are taken and created into the database. (Anonymous way)
 */
@Table(
    name = "JBH_USER",
    uniqueConstraints = [(UniqueConstraint(columnNames = ["nick_name", "email"]))]
)
data class User(
    @org.hibernate.annotations.GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "UUID")
    @Id
    val id: UUID? = null,

    @Column(name = "user_name")
    @get:Size(
        min = DefaultValues.MIN_DESC_NAMES_LENGTH,
        message = "Name should have at least ${DefaultValues.MIN_DESC_NAMES_LENGTH} and maximum ${DefaultValues.MAX_DESC_NAMES_LENGTH} characters",
        max = DefaultValues.MAX_DESC_NAMES_LENGTH
    )
    val userName: String = DefaultValues.EMPTY_STRING,

    @Column(name = "email", unique = true, nullable = false, updatable = true)
    @get:NotEmpty(message = "Email can not be empty")
    @field:Email(message = "Email should be valid")
    val email: String = DefaultValues.EMPTY_STRING,

    @Column(name = "nick_name")
    @get:NotEmpty(message = "User nickName can not be empty")
    @get:Size(
        min = 3,
        message = "Name should have at least ${DefaultValues.MIN_DESC_NAMES_LENGTH} and maximum ${DefaultValues.MAX_DESC_NAMES_LENGTH} characters",
        max = DefaultValues.MAX_DESC_NAMES_LENGTH
    )
    val nickName: String = DefaultValues.EMPTY_STRING,

    @Column(name = "password", nullable = false)
    @get:NotNull(message = "Password can not be null")
    @get:NotEmpty(message = "Password can not be empty")
    var password: String = DefaultValues.EMPTY_STRING,

    @ManyToMany(fetch = FetchType.LAZY, cascade = arrayOf(CascadeType.ALL))
    @JoinTable(
        name = "user_roles",
        joinColumns = [JoinColumn(name = "user_id")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    var roles: Set<Role> = mutableSetOf(Role())

) : AuditModel(), DTOConverter<UserDTO, User> {

    override fun toDTO(): UserDTO {
        return UserDTO(
            id = this.id,
            userName = this.userName,
            password = this.password,
            email = this.email,
            nickName = this.nickName,
            roles = this.roles,
            createdAt = this.createdAt

        )
    }

    fun isAdmin(): Boolean = this.roles.map { it.name }.contains(RoleName.ROLE_ADMIN)
}
