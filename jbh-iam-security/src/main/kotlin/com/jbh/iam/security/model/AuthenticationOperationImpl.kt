package com.jbh.iam.security.model

import com.jbh.iam.core.access.AuthenticationOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

@Configuration
class AuthenticationOperationImpl : AuthenticationOperation {

    @Autowired
    lateinit var authenticationManager: AuthenticationManager

    @Autowired
    lateinit var tokenProvider: JwtTokenProvider

    companion object {
        val log = org.slf4j.LoggerFactory.getLogger(AuthenticationOperationImpl::class.java)
    }

    override fun authenticate(usernameOrEmail: String, password: String): String {
        log.info("Authenticating user $usernameOrEmail")
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                usernameOrEmail,
                password
            )
        )
        SecurityContextHolder.getContext().authentication = authentication

        val jwt = tokenProvider.generateToken(authentication)
        log.info("Authentication successful. Returning JWT token.")

        return jwt // Change to an object with the result
    }


}