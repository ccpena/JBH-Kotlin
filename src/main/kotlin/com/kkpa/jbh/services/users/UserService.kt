package com.kkpa.jbh.services.users

import com.kkpa.jbh.dto.UserDTO

interface UserService {

    fun findByUniqueUser(userDTO: UserDTO): UserDTO?

    fun existsByNickName(nickName: String): Boolean

    fun existsByEmail(email: String): Boolean
}