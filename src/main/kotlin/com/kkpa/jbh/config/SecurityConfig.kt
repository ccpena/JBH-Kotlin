package com.kkpa.jbh.config

import com.kkpa.jbh.security.CustomUserDetailsService
import com.kkpa.jbh.security.JwtAuthenticationEntryPoint
import com.kkpa.jbh.security.JwtAuthenticationFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.BeanIds
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity // This is the primary spring security annotation that is used to enable web security in a project.
@EnableGlobalMethodSecurity(
    securedEnabled = true, // The securedEnabled property determines if the @Secured annotation should be enabled
    jsr250Enabled = true, // The jsr250Enabled property allows us to use the @RoleAllowed annotation
    prePostEnabled = true // The prePostEnabled property enables Spring Security pre/post annotations
)
// TODO  https://www.baeldung.com/spring-security-method-security
class SecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    lateinit var customUserDetailsService: CustomUserDetailsService

    @Autowired
    lateinit var unauthorizedHandler: JwtAuthenticationEntryPoint

    /**
     * AuthenticationManagerBuilder is used to create an AuthenticationManager instance which is the main Spring Security interface for authenticating a user.
     * You can use AuthenticationManagerBuilder to build in-memory authentication, LDAP authentication, JDBC authentication, or add your custom authentication provider.
     */
    override fun configure(authenticationManagerBuilder: AuthenticationManagerBuilder?) {
        val userDetails = authenticationManagerBuilder?.userDetailsService(customUserDetailsService)
        val passwordEncoder = passwordEncoder()
        userDetails!!.passwordEncoder(passwordEncoder)
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    private val AUTH_WHITELIST = arrayOf(
        // -- swagger ui
        "/v2/api-docs",
        "/swagger-resources",
        "/swagger-resources/**",
        "/configuration/ui",
        "/configuration/security",
        "/swagger-ui.html",
        "/webjars/**"
    ) // other public endpoints of your API may be appended to this array

    override fun configure(http: HttpSecurity) {
        http
            .cors()
            .and()
            .csrf()
            .disable()
            .exceptionHandling()
            .authenticationEntryPoint(unauthorizedHandler)
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .antMatcher("/**")
            .authorizeRequests()
            .antMatchers(
                "/",
                "/favicon.ico",
                "/**/*.png",
                "/**/*.gif",
                "/**/*.svg",
                "/**/*.jpg",
                "/**/*.html",
                "/**/*.css",
                "/**/*.js"
            )
            .permitAll()
            .antMatchers("/jbh/auth/**").permitAll()
            .antMatchers("/auth/**").permitAll()
            .antMatchers(*AUTH_WHITELIST).permitAll()
            .antMatchers(HttpMethod.GET, "/party-deserialization/**").permitAll()
            .anyRequest()
            .authenticated()

        // Persistent login via cookie and redis
        // http.rememberMe().rememberMeServices(rememberMeService).useSecureCookie(false)

        // Add our custom JWT security filter
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter::class.java)
    }

    @Bean
    fun jwtAuthenticationFilter(): JwtAuthenticationFilter {
        return JwtAuthenticationFilter()
    }
}