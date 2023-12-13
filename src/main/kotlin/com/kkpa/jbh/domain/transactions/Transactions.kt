package com.kkpa.jbh.domain.transactions

import com.kkpa.jbh.domain.AuditModel
import com.kkpa.jbh.domain.DTOConverter
import com.kkpa.jbh.domain.accounts.Accounts
import com.kkpa.jbh.domain.categories.SubCategories
import com.kkpa.jbh.dto.TransactionDTO
import com.kkpa.jbh.util.DefaultValues
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "transactions")
data class Transactions(

    @org.hibernate.annotations.GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "UUID")
    @Id
    val id: UUID? = null,

    @Column(name = "description")
    @field:NotEmpty(message = "Description is required") // See https://stackoverflow.com/questions/35847763/kotlin-data-class-bean-validation-jsr-303
    @field:Size(
        min = DefaultValues.MIN_DESC_NAMES_LENGTH,
        message = "Description should have at least ${DefaultValues.MIN_DESC_NAMES_LENGTH} characters",
        max = DefaultValues.MAX_DESC_NAMES_LENGTH
    )
    val description: String,

    @Column(name = "effective_date", nullable = false)
    @field:NotNull(message = "Effective date can be null")
    val effectiveDate: LocalDate? = null,

    @ManyToOne
    @JoinColumn(name = "account_id")
    val account: Accounts,

    @ManyToOne
    @JoinColumn(name = "sub_category_id")
    val subCategory: SubCategories,

    @Column(name = "total_value")
    val totalValue: BigDecimal

) : AuditModel(), DTOConverter<TransactionDTO, Transactions> {

    override fun toDTO(): TransactionDTO {
        return TransactionDTO(
            id = this.id,
            description = this.description,
            effectiveDate = this.effectiveDate,
            accountId = this.account.id,
            subCategoryDTO = this.subCategory.toDTO(),
            totalValue = this.totalValue
        )
    }
}