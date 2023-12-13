package com.kkpa.jbh.repositories.transactions

import com.kkpa.jbh.domain.accounts.Accounts
import com.kkpa.jbh.domain.transactions.Transactions
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface TransactionsRepository : JpaRepository<Transactions, UUID> {

    fun findByAccount(accounts: Accounts): List<Transactions>
}