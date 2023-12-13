package com.kkpa.jbh.security

import com.fasterxml.jackson.annotation.JsonIgnore
import com.kkpa.jbh.domain.Users.User
import com.kkpa.jbh.domain.Users.UsersGroup
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.UUID
import kotlin.streams.toList

data class UserPrincipal(
    val id: UUID?,
    val userGroup: UsersGroup?,
    val name: String,
    private val userName: String,
    @JsonIgnore
    val email: String,
    @JsonIgnore
    val jbhPassword: String,
    val jbhAuthorities: List<SimpleGrantedAuthority>
) : UserDetails {

    companion object {
        fun create(user: User): UserPrincipal {
            val authorities = user.roles.stream().map { role -> SimpleGrantedAuthority(role.name.toString()) }.toList()

            return UserPrincipal(
                id = user.id,
                userGroup = null,
                name = user.userName,
                userName = user.nickName,
                email = user.email,
                jbhPassword = user.password,
                jbhAuthorities = authorities
            )
        }

        fun create(userGroup: UsersGroup): UserPrincipal {
            val user = userGroup.userOwner
            val authorities = user.roles.stream().map { role -> SimpleGrantedAuthority(role.name.toString()) }.toList()

            return UserPrincipal(
                id = user.id,
                userGroup = userGroup,
                name = user.userName,
                userName = user.nickName,
                email = user.email,
                jbhPassword = user.password,
                jbhAuthorities = authorities
            )
        }
    }

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun getUsername(): String = userName

    override fun getPassword(): String = jbhPassword

    override fun getAuthorities(): List<SimpleGrantedAuthority> = jbhAuthorities

    override fun isEnabled(): Boolean = true
}