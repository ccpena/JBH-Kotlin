package com.kkpa.jbh.controllers.accounts

import com.kkpa.jbh.controllers.Routes
import com.kkpa.jbh.dto.AccountsDTO
import com.kkpa.jbh.security.CurrentUser
import com.kkpa.jbh.security.UserPrincipal
import com.kkpa.jbh.services.accounts.AccountService
import com.kkpa.jbh.services.accounts.AccountServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping(Routes.ACCOUNT_PATH)
class AccountController(@Autowired private val accountService: AccountServiceImpl) {

    @GetMapping(Routes.TOTAL_ACCOUNTS)
    fun getTotalAccountsByUser(@CurrentUser currentUser: UserPrincipal): ResponseEntity<Int> {
        val accountList = accountService.findAccountsByUserGroupId(currentUser.userGroup!!.id!!)
        return ResponseEntity.ok(accountList.size)
    }

    @GetMapping
    fun getAllAccounts(@CurrentUser currentUser: UserPrincipal): ResponseEntity<*> {
        println(currentUser)
        val accountList = accountService.findAccountsByUserGroupId(currentUser.userGroup!!.id!!)
        return ResponseEntity.ok(accountList)
    }

    @GetMapping("/{id}")
    fun getAccount(@PathVariable id: UUID): ResponseEntity<AccountsDTO> {
        val accountDTO = accountService.findById(id)

        return ResponseEntity.ok(accountDTO!!)
    }
}