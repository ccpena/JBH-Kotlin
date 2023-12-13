package com.kkpa.jbh.domain.accounts

import com.kkpa.jbh.domain.AuditModel
import com.kkpa.jbh.domain.DTOConverter
import com.kkpa.jbh.domain.Users.UsersGroup
import com.kkpa.jbh.domain.toDTOList
import com.kkpa.jbh.domain.transactions.Transactions
import com.kkpa.jbh.dto.AccountsDTO
import com.kkpa.jbh.util.DefaultValues.EMPTY_STRING
import com.kkpa.jbh.util.DefaultValues.MAX_DESC_NAMES_LENGTH
import com.kkpa.jbh.util.DefaultValues.MIN_DESC_NAMES_LENGTH
import java.math.BigDecimal
import java.util.UUID
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

@Entity
data class Accounts(

    @org.hibernate.annotations.GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "UUID")
    @Id
    val id: UUID? = null,

    @Column(name = "description")
    @field:NotEmpty(message = "Description is required") // See https://stackoverflow.com/questions/35847763/kotlin-data-class-bean-validation-jsr-303
    @field:Size(
        min = MIN_DESC_NAMES_LENGTH,
        message = "Description should have at least $MIN_DESC_NAMES_LENGTH characters",
        max = MAX_DESC_NAMES_LENGTH
    )
    val description: String = EMPTY_STRING,

    @Column(name = "active_balance")
    val activeBalance: BigDecimal = BigDecimal.ZERO,

    @Column(name = "passive_balance")
    val passiveBalance: BigDecimal = BigDecimal.ZERO,

    @OneToOne
    @JoinColumn(name = "user_group_id")
    val userGroup: UsersGroup = UsersGroup(),

    @OneToMany(cascade = arrayOf(CascadeType.ALL), mappedBy = "account")
    val transactions: MutableList<Transactions> = mutableListOf()

) : AuditModel(), DTOConverter<AccountsDTO, Accounts> {

    override fun toDTO(): AccountsDTO {
        return AccountsDTO(
            this.id,
            this.description,
            this.activeBalance,
            this.passiveBalance,
            this.userGroup.toDTO(),
            this.transactions.toDTOList().toMutableList()
        )
    }

    fun addTransaction(transaction: Transactions) = transactions.add(transaction)
}