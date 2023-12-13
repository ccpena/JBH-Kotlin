package com.kkpa.jbh.repositories.accounts

import com.kkpa.jbh.domain.accounts.Accounts
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface AccountRepository : JpaRepository<Accounts, UUID> {

    fun findByUserGroupId(userGroupId: UUID): List<Accounts>
}