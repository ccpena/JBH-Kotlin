package com.kkpa.jbh.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * We’ll use JWTAuthenticationFilter to implement a filter that -

 *   reads JWT authentication token from the Authorization header of all the requests
 *   validates the token
 *   loads the user details associated with that token.
 *   Sets the user details in Spring Security’s SecurityContext.
 *  Spring Security uses the user details to perform authorization checks.
 *  We can also access the user details stored in the SecurityContext in our controllers to perform our business logic.
 */
class JwtAuthenticationFilter : OncePerRequestFilter() {

    @Autowired
    lateinit var tokenProvider: JwtTokenProvider

    @Autowired
    lateinit var customUserDetailsService: CustomUserDetailsService

    /**
     * We’re first parsing the JWT retrieved from the Authorization header of the request and obtaining the user’s Id. After that,
     * We’re loading the user’s details from the database and setting the authentication inside spring security’s context.
     *
     *
     *
     */
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        // TODO Figure out why SecurityConfig is not working, or centralize it for a while.

        if (!request.requestURI.startsWith("/jbh/auth/")) {

            try {
                val jwt = getJwtFromRequest(request)

                if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                    val userId = tokenProvider.getUserIdFromJWT(jwt)

                    /*
                        Note that you could also encode the user's username and roles inside JWT claims
                        and create the UserDetails object by parsing those claims from the JWT.
                        That would avoid the following database hit. It's completely up to you.
                     */
                    val userDetails = customUserDetailsService.loadUserById(userId!!)
                    val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                    authentication.details = WebAuthenticationDetailsSource().buildDetails(request)

                    SecurityContextHolder.getContext().authentication = authentication
                }
            } catch (ex: Exception) {
                logger.error("Could not set user authentication in security context", ex)
            }
        }
        filterChain.doFilter(request, response)
    }

    private fun getJwtFromRequest(request: HttpServletRequest): String {
        val bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION)
        bearerToken.startsWith("Bearer ").let {
            return bearerToken.substring(7, bearerToken.length)
        }
    }
}