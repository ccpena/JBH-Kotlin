package com.kkpa.jbh.domain.Users

import com.kkpa.jbh.domain.AuditModel
import com.kkpa.jbh.domain.DTOConverter
import com.kkpa.jbh.dto.UserGroupDTO
import com.kkpa.jbh.util.DefaultValues
import com.kkpa.jbh.util.DefaultValues.MAX_DESC_NAMES_LENGTH
import com.kkpa.jbh.util.DefaultValues.MIN_DESC_NAMES_LENGTH
import java.util.UUID
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToOne
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Entity
data class UsersGroup(

    @org.hibernate.annotations.GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "UUID")
    @Id
    val id: UUID? = null,

    @Column(name = "NAME")
    @get:NotBlank(message = "User Group nickName is required")
    @get:Size(
        min = MIN_DESC_NAMES_LENGTH,
        max = MAX_DESC_NAMES_LENGTH,
        message = "Name should have be between [$MIN_DESC_NAMES_LENGTH - $MAX_DESC_NAMES_LENGTH]"
    )
    val name: String = DefaultValues.EMPTY_STRING,

    /**
     * Identify it the group is just for the user.
     */
    @Column(name = "single")
    val single: Boolean = true,

    @OneToOne(cascade = [(CascadeType.REFRESH), (CascadeType.DETACH), (CascadeType.MERGE)])
    @JoinColumn(name = "user_id")
    @field:Valid // To Valid entity user against constraint violations exceptions when it's saving an userGroupd
    val userOwner: User = User()

) : AuditModel(), DTOConverter<UserGroupDTO, UsersGroup> {

    override fun toDTO(): UserGroupDTO {
        return UserGroupDTO(
            this.id,
            this.name,
            this.single,
            this.userOwner.toDTO()
        )
    }
}
