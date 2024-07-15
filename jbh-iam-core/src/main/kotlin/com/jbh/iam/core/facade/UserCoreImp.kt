package com.jbh.iam.core.facade

import org.springframework.stereotype.Component
import java.util.*

@Component
class UserCoreImp : UsersFacade {
    override fun findByNickNameOrEmail(nickName: String, email: String): UserCore? {
        TODO("Not yet implemented")
    }

    override fun findByUserOwnerId(id: UUID): UserGroupCore? {
        TODO("Not yet implemented")
    }
}