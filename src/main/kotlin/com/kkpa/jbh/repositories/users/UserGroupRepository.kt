package com.kkpa.jbh.repositories.users

import com.kkpa.jbh.domain.Users.User
import com.kkpa.jbh.domain.Users.UsersGroup
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface UserGroupRepository : JpaRepository<UsersGroup, UUID> {

    fun findByUserOwnerAndSingle(userOwner: User, single: Boolean): UsersGroup?

    fun findByNameAndSingle(name: String, single: Boolean): UsersGroup?

    fun findByUserOwnerNickNameOrUserOwnerEmail(nickName: String, email: String): UsersGroup?

    fun findByUserOwnerId(id: UUID): UsersGroup?
}
