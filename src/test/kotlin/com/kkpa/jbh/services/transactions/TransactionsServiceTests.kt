package com.kkpa.jbh.services.transactions

import com.kkpa.jbh.domain.transactions.Transactions
import com.kkpa.jbh.dto.SubCategoryDTO
import com.kkpa.jbh.dto.TransactionDTO
import com.kkpa.jbh.services.ServicesTests
import com.kkpa.jbh.services.accounts.AccountServiceTests.Companion.saveDefaultAccountDomain
import com.kkpa.jbh.services.categories.SubCategoriesServiceTests.Companion.saveAndGetDefaultSubCategoryDomain
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

class TransactionsServiceTests : ServicesTests() {

    val DEFAULT_DESCRIPTION = "Transaction Default "
    val DEFAULT_TOTAL_VALUE = BigDecimal("1000.5898")

    @Autowired
    lateinit var transactionsServiceImpl: TransactionsServiceImpl

    fun createDefaultTransactionDTO(
        desc: String = DEFAULT_DESCRIPTION,
        totalValue: BigDecimal = DEFAULT_TOTAL_VALUE
    ): TransactionDTO {

        val subCategorySaved = saveAndGetDefaultSubCategoryDomain()
        val userGroupSaved = subCategorySaved.category!!.usersGroup

        val accountSaved = saveDefaultAccountDomain(userGroupSaved)

        return TransactionDTO(
            accountId = accountSaved.id,
            subCategoryDTO = subCategorySaved.toDTO(),
            totalValue = totalValue,
            description = desc,
            effectiveDate = LocalDate.now()
        )
    }

    fun transactionOf(
        desc: String = DEFAULT_DESCRIPTION,
        totalValue: BigDecimal = DEFAULT_TOTAL_VALUE,
        accountId: UUID,
        subCategoryId: UUID,
        effectiveDate: LocalDate = LocalDate.now()
    ): TransactionDTO {

        return TransactionDTO(
            accountId = accountId,
            subCategoryDTO = SubCategoryDTO(id = subCategoryId),
            totalValue = totalValue,
            description = desc,
            effectiveDate = effectiveDate
        )
    }

    fun saveDefaultTransactionEntity(
        desc: String = DEFAULT_DESCRIPTION,
        totalValue: BigDecimal = DEFAULT_TOTAL_VALUE
    ): Transactions {
        return transactionsServiceImpl.save(
            createDefaultTransactionDTO(
                desc = desc,
                totalValue = totalValue
            )
        ).toDomain()
    }

    @Before
    fun setup() {
        transactionsServiceImpl.findAll().forEach {
            transactionsServiceImpl.delete(it)
        }
    }

    override fun givenEntityThenShouldBeCreated() {
        saveDefaultTransactionEntity().let {
            assertThat(it.id).isNotNull()
            assertThat(it.description).isEqualTo(DEFAULT_DESCRIPTION)
            assertThat(it.totalValue).isEqualTo(DEFAULT_TOTAL_VALUE)
            assertThat(it.account.id).isNotNull()
            assertThat(it.subCategory.id).isNotNull()
        }
    }

    override fun givenEntityUpdateItShouldBeSuccessful() {
        val transactionBD = saveDefaultTransactionEntity()
        val description = "TransactionUpdated"
        val newTotalValue = BigDecimal.valueOf(85698.54)
        val transactionToBeUpdated = transactionBD.copy(description = description, totalValue = newTotalValue)

        transactionsServiceImpl.update(transactionToBeUpdated.toDTO())
            .let {
                assertThat(it.id).isNotNull()
                assertThat(it.description).isEqualTo(description)
                assertThat(it.totalValue).isEqualTo(newTotalValue)
                assertThat(it.accountId).isEqualTo(transactionBD.account.id)
                assertThat(it.subCategoryDTO?.id).isEqualTo(transactionBD.subCategory.id)
            }
    }

    override fun givenEntityAgainstConstraintsThenSaveItShouldThrowException() {
        saveDefaultTransactionEntity(desc = "Sho")
    }

    override fun givenNotExistingEntityFindByIdShouldReturnNull() {
        transactionsServiceImpl.findById(UUID.randomUUID()).let {
            assertThat(it).isNull()
        }
    }

    override fun givenEntityAgainstConstraintsThenUpdateItShouldThrowException() {
        val transactionBD = saveDefaultTransactionEntity()
        val description = "XX"
        val newTotalValue = BigDecimal.valueOf(85698.54)
        val transactionToBeUpdated = transactionBD.copy(description = description, totalValue = newTotalValue)

        transactionsServiceImpl.update(transactionToBeUpdated.toDTO())
    }

    override fun giveEntityCreatedThenDeleteIt() {
        saveDefaultTransactionEntity().let {
            transactionsServiceImpl.delete(it.toDTO()).let {
                assertThat(it).isTrue()
            }
        }
    }

    override fun givenEntityNotExistingThenDeleteItShouldThrowAnException() {
        transactionsServiceImpl.delete(TransactionDTO(id = UUID.randomUUID()))
    }

    override fun givenEntityWithIdNullThenDeleteItShouldThrowAnException() {
        transactionsServiceImpl.delete(TransactionDTO())
    }

    override fun givenNotUniqueEntityThenSaveItShouldThrowAnException() {
        throw IllegalArgumentException("Not defined yet")
    }

    override fun givenNotUniqueEntityThenUpdateItShouldThrowAnException() {
        throw IllegalArgumentException("Not defined yet")
    }

    @Test
    fun addMultiplesTransactionsToAnAccount() {
        val defaultTransaction = saveDefaultTransactionEntity()
        val accountId = defaultTransaction.account.id
        val subCategoryId = defaultTransaction.subCategory.id

        val count = 5
        for (i in 1..count) {
            transactionsServiceImpl.save(
                transactionOf(
                    desc = "Another tx {i}",
                    totalValue = i.toBigDecimal(),
                    subCategoryId = subCategoryId!!,
                    accountId = accountId!!
                )
            )
        }

        assertThat(transactionsServiceImpl.findByAccount(accountId!!)).hasSize(count.plus(1))
    }

    @Test
    fun givenFakeAccountFindByAccountIdShouldBeEmpty() {
        assertThat(transactionsServiceImpl.findByAccount(UUID.randomUUID())).hasSize(0)
    }
}