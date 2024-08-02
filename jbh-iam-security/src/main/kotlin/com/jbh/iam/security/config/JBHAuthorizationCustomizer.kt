package com.jbh.iam.security.config

import org.slf4j.LoggerFactory
import org.springframework.http.HttpMethod
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
) :
    Customizer<AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry> {

    companion object {
        private val log = LoggerFactory.getLogger(JBHAuthorizationCustomizer::class.java)
    }

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

        log.info("Registering authorization rules")
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
