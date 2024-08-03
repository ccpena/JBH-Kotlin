package com.jbh.iam.api.services

import com.jbh.iam.api.services.users.UserService
import com.jbh.iam.core.access.UserDataAccess
import com.jbh.iam.core.facade.UserCore
import com.jbh.iam.core.facade.UserGroupCore
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserDataAccessImpl : UserDataAccess {

    @Autowired
    lateinit var userService: UserService

    companion object {
        private val log = LoggerFactory.getLogger(UserDataAccess::class.java)
    }

    override fun findByNickNameOrEmail(nickName: String, email: String): UserCore? {
        log.info("loading user by nickName or email... ")
        val user = userService.findByNickNameOrEmail(nickName, email)
            ?: throw IllegalArgumentException("User not found with username or email $email")

        log.info("loading user by userName... User Found! ${user.nickName}")
        return user
    }

    override fun findByUserOwnerId(id: UUID): UserGroupCore? {
        log.info("loading user by ownerId... ")
        val userGroup = userService.findByUserOwnerId(id)
            ?: throw IllegalArgumentException("User not found with id : $id")

        log.info("loading user by ownerId... User Found! ${userGroup.userOwner?.nickName}")
        return userGroup
    }
}