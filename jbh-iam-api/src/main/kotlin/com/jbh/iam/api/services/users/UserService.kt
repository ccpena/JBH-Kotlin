package com.jbh.iam.api.services.users

import com.jbh.iam.api.dto.UserDTO
import com.jbh.iam.core.facade.UserCore
import com.jbh.iam.core.facade.UserGroupCore
import java.util.*

interface UserService {

    fun findByUniqueUser(userDTO: UserDTO): UserDTO?

    fun existsByNickName(nickName: String): Boolean

    fun existsByEmail(email: String): Boolean

    fun findByNickNameOrEmail(nickName: String, email: String): UserCore?

    fun findByUserOwnerId(id: UUID): UserGroupCore?
}