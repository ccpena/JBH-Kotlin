package com.kkpa.jbh.services.accounts

import com.kkpa.jbh.domain.Users.UsersGroup
import com.kkpa.jbh.domain.accounts.Accounts
import com.kkpa.jbh.dto.AccountsDTO
import com.kkpa.jbh.extensions.generateRandom
import com.kkpa.jbh.services.ServicesTests
import com.kkpa.jbh.services.users.UserGroupServiceImpl
import com.kkpa.jbh.services.users.UserServicesTests.Companion.createUserGroupDomain
import com.kkpa.jbh.util.DefaultValues.DEFAULT_WORTH
import org.assertj.core.api.Assertions.assertThat
import org.junit.Ignore
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import java.math.BigDecimal
import java.util.UUID

class AccountServiceTests : ServicesTests() {

    @Autowired
    lateinit var accountServiceImpl: AccountServiceImpl

    @Autowired
    lateinit var userGroupService: UserGroupServiceImpl

    companion object {
        const val DEFAULT_ACCOUNT_NAME = "Saving Account"

        private val accountServiceImpl: AccountServiceImpl by lazy {
            if (context == null) throw IllegalAccessException("context not found")
            context!!.getBean(AccountServiceImpl::class.java)
        }

        fun saveDefaultAccountDomain(userGroup: UsersGroup): Accounts {
            return accountServiceImpl.save(createDefaultAccountEntity(userGroup = userGroup).toDTO())
                .toDomain()
        }

        fun createDefaultAccountEntity(accountId: UUID? = null, userGroup: UsersGroup): Accounts =
            Accounts(id = accountId, description = DEFAULT_ACCOUNT_NAME, userGroup = userGroup)
    }

    private fun getFakeUserGroup(): UsersGroup = UsersGroup(id = UUID.randomUUID())

    fun saveDefaultEntity(): AccountsDTO {
        val userGroupCreated = createUserGroupDomain()
        val accountToBeCreated = createDefaultAccountEntity(userGroup = userGroupCreated)

        return accountServiceImpl.save(accountToBeCreated.toDTO())
    }

    @Test(expected = IllegalArgumentException::class)
    fun createAccountWithoutUserGroup() {
        val accountWithoutUser = createDefaultAccountEntity(userGroup = UsersGroup())
        accountServiceImpl.save(accountWithoutUser.toDTO())
    }

    @Test
    override fun givenEntityThenShouldBeCreated() {
        val defaultAccountCreated = saveDefaultEntity()

        val accountCreated = accountServiceImpl.findById(defaultAccountCreated.id!!)

        assertThat(accountCreated).isNotNull

        accountCreated?.let {
            assertThat(it.id).isNotNull()
            assertThat(it.description).isEqualTo(DEFAULT_ACCOUNT_NAME)
            assertThat(it.activeBalance).isEqualTo(DEFAULT_WORTH)
            assertThat(it.passiveBalance).isEqualTo(DEFAULT_WORTH)
            assertThat(it.userGroup.id).isNotNull()
        }
    }

    override fun givenEntityAgainstConstraintsThenSaveItShouldThrowException() {
        val badAccountToBeCreated = Accounts(description = "", userGroup = getFakeUserGroup())
        accountServiceImpl.save(badAccountToBeCreated.toDTO())
    }

    @Test
    override fun givenNotExistingEntityFindByIdShouldReturnNull() {
        val accountShouldBeNull = accountServiceImpl.findById(UUID.randomUUID())
        assertThat(accountShouldBeNull).isNull()
    }

    @Test
    fun findAccountsByUserGroupIdShouldBeSuccess() {
        val defaultAccountCreated = saveDefaultEntity()
        val userGroupId = defaultAccountCreated.userGroup.id

        for (i in 1..5) {
            val accountToBeCreated = defaultAccountCreated.copy(description = "Account {i}", id = null)
            accountServiceImpl.save(accountToBeCreated)
        }

        val accountsByUserGroup = accountServiceImpl.findAccountsByUserGroupId(userGroupId!!)
        assertThat(accountsByUserGroup).size().isEqualTo(6)
    }

    @Test
    override fun givenEntityUpdateItShouldBeSuccessful() {
        var defaultAccountCreated = saveDefaultEntity()

        val activeBalanceForUpdate = BigDecimal.ONE.generateRandom()
        val passiveBalanceForUpdate = BigDecimal.TEN.generateRandom()
        val newDescription = "Description Updated"
        var defaultAccountUpdated = defaultAccountCreated.copy(
            activeBalance = activeBalanceForUpdate,
            passiveBalance = passiveBalanceForUpdate,
            description = newDescription
        )

        accountServiceImpl.update(defaultAccountUpdated).let {
            assertThat(it.id).isEqualTo(defaultAccountCreated.id)
            assertThat(it.activeBalance).isEqualTo(activeBalanceForUpdate)
            assertThat(it.description).isEqualTo(newDescription)
            assertThat(it.passiveBalance).isEqualTo(passiveBalanceForUpdate)
        }
    }

    override fun givenEntityAgainstConstraintsThenUpdateItShouldThrowException() {
        val defaultAccountCreated = saveDefaultEntity()

        val activeBalanceForUpdate = BigDecimal.ONE.generateRandom()
        val passiveBalanceForUpdate = BigDecimal.TEN.generateRandom()
        val newDescription = ""
        var defaultAccountUpdated = defaultAccountCreated.copy(
            activeBalance = activeBalanceForUpdate,
            passiveBalance = passiveBalanceForUpdate,
            description = newDescription
        )

        accountServiceImpl.update(defaultAccountUpdated)
    }

    @Test
    override fun giveEntityCreatedThenDeleteIt() {
        val defaultAccountCreated = saveDefaultEntity()

        defaultAccountCreated.id?.let {
            accountServiceImpl.delete(defaultAccountCreated)
            assertThat(accountServiceImpl.findById(it)).isNull()
        }
    }

    @Test(expected = IllegalArgumentException::class)
    override fun givenEntityNotExistingThenDeleteItShouldThrowAnException() {
        var accountNotExisting =
            createDefaultAccountEntity(accountId = UUID.randomUUID(), userGroup = getFakeUserGroup())

        accountNotExisting.id?.let {
            accountServiceImpl.delete(accountNotExisting.toDTO())
        }
    }

    @Test(expected = IllegalArgumentException::class)
    override fun givenEntityWithIdNullThenDeleteItShouldThrowAnException() {
        var accountNotExisting = createDefaultAccountEntity(userGroup = getFakeUserGroup())
        accountServiceImpl.delete(accountNotExisting.toDTO())
    }

    @Ignore
    override fun givenNotUniqueEntityThenSaveItShouldThrowAnException() {
        throw IllegalArgumentException("Account can be equals for an user group")
    }

    @Ignore
    override fun givenNotUniqueEntityThenUpdateItShouldThrowAnException() {
        throw IllegalArgumentException("Account can be equals for an user group")
    }
}