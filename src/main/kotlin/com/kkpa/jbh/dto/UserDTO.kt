package com.kkpa.jbh.dto

import com.kkpa.jbh.domain.Users.Role
import com.kkpa.jbh.domain.Users.User
import com.kkpa.jbh.util.DefaultValues.EMPTY_STRING
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.UUID

@Component
data class UserDTO(
    val id: UUID? = null,
    val userName: String = EMPTY_STRING,
    val email: String = EMPTY_STRING,
    var password: String = EMPTY_STRING,
    val nickName: String = EMPTY_STRING,
    val roles: Set<Role> = emptySet(),
    val createdAt: LocalDateTime = LocalDateTime.now()
) : JbhDTO, DomainConverter<UserDTO, User> {

    override fun toDomain(): User {
        var user = User(
            id = this.id,
            userName = this.userName,
            email = this.email,
            password = this.password,
            nickName = this.nickName,
            roles = this.roles
        )
        user.createdAt = this.createdAt;
        return user;
    }
}
