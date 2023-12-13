package com.kkpa.jbh.repositories.users

import com.kkpa.jbh.domain.Users.Role
import com.kkpa.jbh.domain.Users.RoleName
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.Optional
import java.util.UUID

@Repository
interface RoleRepository : JpaRepository<Role, UUID> {
    fun findByName(roleName: RoleName): Optional<Role>
}