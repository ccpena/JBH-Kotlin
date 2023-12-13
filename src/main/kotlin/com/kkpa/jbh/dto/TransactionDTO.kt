package com.kkpa.jbh.dto

import com.kkpa.jbh.domain.accounts.Accounts
import com.kkpa.jbh.domain.categories.SubCategories
import com.kkpa.jbh.domain.transactions.Transactions
import com.kkpa.jbh.util.DefaultValues
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

data class TransactionDTO(
    val id: UUID? = null,
    val accountId: UUID? = null,
    val subCategoryDTO: SubCategoryDTO? = null,
    val totalValue: BigDecimal = BigDecimal.ZERO,
    val description: String = DefaultValues.EMPTY_STRING,
    val effectiveDate: LocalDate? = null
) : DomainConverter<TransactionDTO, Transactions> {
    override fun toDomain(): Transactions {
        return Transactions(
            id = this.id,
            account = Accounts(id = this.accountId),
            subCategory = SubCategories(id = this.subCategoryDTO?.id),
            totalValue = this.totalValue,
            description = this.description,
            effectiveDate = this.effectiveDate
        )
    }
}