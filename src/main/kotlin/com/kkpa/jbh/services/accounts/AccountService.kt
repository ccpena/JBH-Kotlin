package com.kkpa.jbh.services.accounts

import com.kkpa.jbh.dto.AccountsDTO
import java.util.UUID

interface AccountService {

    fun findAccountsByUserGroupId(userGroupId: UUID): List<AccountsDTO>
}