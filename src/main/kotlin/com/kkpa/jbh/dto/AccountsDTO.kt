package com.kkpa.jbh.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.kkpa.jbh.domain.accounts.Accounts
import com.kkpa.jbh.util.DefaultValues
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.util.UUID

@Component
data class AccountsDTO(

    val id: UUID? = null,
    val description: String = DefaultValues.EMPTY_STRING,
    val activeBalance: BigDecimal = BigDecimal.ZERO,
    val passiveBalance: BigDecimal = BigDecimal.ZERO,
    @JsonIgnoreProperties
    @JsonIgnore
    val userGroup: UserGroupDTO = UserGroupDTO(),
    val transactions: MutableList<TransactionDTO> = mutableListOf()

) : JbhDTO, DomainConverter<AccountsDTO, Accounts> {

    override fun toDomain(): Accounts {
        val accountDB = Accounts(
            this.id,
            this.description,
            this.activeBalance,
            this.passiveBalance,
            this.userGroup.toDomain()
        )
        transactions.forEach {
            accountDB.addTransaction(it.toDomain())
        }

        return accountDB
    }
}