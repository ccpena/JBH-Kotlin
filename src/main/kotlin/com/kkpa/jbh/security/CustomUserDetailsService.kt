package com.kkpa.jbh.security

import com.kkpa.jbh.repositories.users.UserGroupRepository
import com.kkpa.jbh.repositories.users.UserRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.UUID
import javax.transaction.Transactional

/**
 * To authenticate a User or perform various role-based checks, Spring security needs to load users details somehow.
 *
 * For this purpose, It consists of an interface called UserDetailsService which has a single method that loads a user based on username-
 *
 * Note that, the loadUserByUsername() method returns a UserDetails object that Spring Security uses for performing various authentication and role based validations.

In our implementation, We’ll also define a custom UserPrincipal class that will implement UserDetails interface, and return the UserPrincipal object from loadUserByUsername() method.
 *
 */
@Service
class CustomUserDetailsService : UserDetailsService {

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var userGroupRepository: UserGroupRepository

    companion object {
        val log: Logger = LoggerFactory.getLogger(CustomUserDetailsService::class.java)
    }

    /**
     * It's used by Spring Security when the user try to login.
     */
    override fun loadUserByUsername(userNameOrEmail: String): UserDetails {
        val user = userRepository.findByNickNameOrEmail(userNameOrEmail, userNameOrEmail)
            ?: throw UsernameNotFoundException("User not found with username or email $userNameOrEmail")

        log.info("loading user by userName... User Found! ${user.nickName}")
        return UserPrincipal.create(user)
    }

    /**
     * This method is executed when a client consume and enpoint once its logged.
     * This method is used by JWTAuthenticationFilter.
     */
    @Transactional
    fun loadUserById(id: UUID): UserDetails {
        val userGroup = userGroupRepository.findByUserOwnerId(id)

        if (userGroup == null) {
            throw UsernameNotFoundException("User not found with id : " + id!!)
        }

        log.info("Loading User by Id... User Found! ${UserPrincipal.create(userGroup)}")
        return UserPrincipal.create(userGroup)
    }
}