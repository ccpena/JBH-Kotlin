package com.jbh.iam.core.facade

import java.util.*

interface UsersFacade {
    fun findByNickNameOrEmail(nickName: String, email: String): UserCore?

    fun findByUserOwnerId(id: UUID): UserGroupCore?
}