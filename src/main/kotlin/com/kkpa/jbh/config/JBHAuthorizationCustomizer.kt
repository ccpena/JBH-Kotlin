package com.kkpa.jbh.config

import com.kkpa.jbh.security.JwtTokenProvider
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer
import org.springframework.stereotype.Component

/**
 * JBHAuthorizationCustomizer.kt:
 *
 * This class customizes the authorization rules for your application.
 */
@Component
class JBHAuthorizationCustomizer(
):
    Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> {

    private val AUTH_WHITELIST = arrayOf(
        // -- swagger ui
        "/v3/api-docs",
        "/swagger-resources/**",
        "/swagger-ui/**",
        "/swagger-ui.html",
        "/service ",
        "/actuator/**",
        "/checkService ",
        "/configuration/ui",
        "/configuration/security",
        "/static/**",
        "/js/**",
        "/css/**",
        "/images/**",
        "/webjars/**"
    ) // other public endpoints of your API may be appended to this array
    override fun customize(registry: AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry) {
        registry.requestMatchers(*AUTH_WHITELIST).permitAll()
        registry.requestMatchers("/admin/**").hasAnyAuthority("ROLE_ADMIN")
        registry.requestMatchers("/user/**").hasAuthority("ROLE_USER")
        registry.requestMatchers("/jbh/auth/**").permitAll()
        registry.requestMatchers("/auth/**").permitAll()
        registry.requestMatchers("/view/**").permitAll()

        registry.requestMatchers(HttpMethod.GET, "/party-deserialization/**").permitAll()
        registry.anyRequest().authenticated()
    }
}
