package com.kkpa.jbh.security

import org.slf4j.LoggerFactory
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Service
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

/**
 * This class is used to return a 401 unauthorized error to clients that try to access a protected resource without proper authentication.
 * It implements Spring Security’s AuthenticationEntryPoint interface.
 */
@Service
class JwtAuthenticationEntryPoint : AuthenticationEntryPoint {

    private val log = LoggerFactory.getLogger(JwtAuthenticationEntryPoint::class.java)

    /**
     * This method is called whenever an exception is thrown due to an unauthenticated user trying to access a resource that requires authentication.
     */
    override fun commence(request: HttpServletRequest?, response: HttpServletResponse, authException: AuthenticationException) {
        log.error("Responding with unauthorized error. Message - {}", authException.message)
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.message)
    }
}