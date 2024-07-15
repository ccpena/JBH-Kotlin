package com.jbh.iam.api.services.users

import com.jbh.iam.api.dto.UserDTO

interface UserService {

    fun findByUniqueUser(userDTO: UserDTO): UserDTO?

    fun existsByNickName(nickName: String): Boolean

    fun existsByEmail(email: String): Boolean
}