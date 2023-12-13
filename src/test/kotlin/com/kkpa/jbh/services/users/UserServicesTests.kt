package com.kkpa.jbh.services.users

import com.kkpa.jbh.domain.Users.UsersGroup
import com.kkpa.jbh.dto.UserDTO
import com.kkpa.jbh.dto.UserGroupDTO
import com.kkpa.jbh.repositories.users.UserGroupRepository
import com.kkpa.jbh.services.ServicesTests
import org.assertj.core.api.Assertions.assertThat
import org.junit.Ignore
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@RunWith(SpringRunner::class)
@SpringBootTest
@Transactional
class UserServicesTests : ServicesTests() {

    @Autowired
    lateinit var userGroupService: UserGroupServiceImpl

    @Autowired
    lateinit var userGroupRepository: UserGroupRepository

    private var usrGroupCreated = UserGroupDTO()

    @Autowired
    lateinit var userServiceImpl: UserServiceImpl

    companion object {

        private val log = LoggerFactory.getLogger(UserServicesTests::class.java)

        private const val DEFAULT_NAME_USER: String = "USER-DEFAULT"
        private const val DEFAULT_NAME_USERGROUP: String = "USER GROUP-DEFAULT"
        const val DEFAULT_EMAIL = "myemail@mytest.com"
        const val DEFAULT_NICK_NAME = "myemail"
        private const val DEFAULT_USER_NAME = "userDefault"
        private const val DEFAULT_PASSWORD = "password1"

        private val userGroupService: UserGroupServiceImpl by lazy {
            if (context == null) throw IllegalAccessException("context not found")
            context!!.getBean(UserGroupServiceImpl::class.java)
        }

        private fun createUserDTO(email: String = DEFAULT_EMAIL, nickName: String = DEFAULT_NICK_NAME): UserDTO {
            val userDTO = UserDTO(
                userName = DEFAULT_NAME_USER,
                email = email,
                password = DEFAULT_PASSWORD,
                nickName = nickName
            )
            return userDTO
        }

        private fun createUserGroupDTO(
            id: UUID? = null,
            name: String = DEFAULT_NAME_USERGROUP,
            userOwner: UserDTO = createUserDTO()
        ): UserGroupDTO {
            return UserGroupDTO(id = id, name = name, single = true, userOwner = userOwner)
        }

        fun createUserGroupDomain(): UsersGroup {
            return userGroupService.save(createUserGroupDTO()).toDomain()
        }

        /**
         * This just will work to tests marked as @Test from jupiter
         */
        @BeforeAll
        @JvmStatic
        internal fun beforeAll() {
            log.info("Removing users created previously")
            userGroupService.findAll().forEach {
                userGroupService.delete(it)
            }
            log.info("Users Group in DB: ${userGroupService.findAll().size}")
        }
    }

    fun saveDefaultEntity(name: String = DEFAULT_NAME_USERGROUP, userOwner: UserDTO = createUserDTO()): UsersGroup {
        return userGroupService.save(createUserGroupDTO(name = name, userOwner = userOwner)).toDomain()
    }

    @Test
    override fun givenEntityThenShouldBeCreated() {
        val userCreated = userGroupService.save(createUserGroupDTO())

        assertThat(userCreated).isNotNull.hasNoNullFieldsOrProperties()
            .hasFieldOrPropertyWithValue("name", DEFAULT_NAME_USERGROUP)

        assertThat(userCreated.id).isNotNull()
    }

    @Test
    fun findUserGroupByUserEmail() {
        val userGroup = saveDefaultEntity()

        userGroupRepository.findByUserOwnerNickNameOrUserOwnerEmail(
            nickName = userGroup.userOwner.email,
            email = userGroup.userOwner.email
        ).let {
            assertThat(it).isNotNull
            assertThat(it?.userOwner).isNotNull
            assertThat(it?.userOwner?.email).isEqualTo(DEFAULT_EMAIL)
        }
    }

    @Test
    fun findUserGroupByNickName() {
        val userGroup = saveDefaultEntity()

        userGroupRepository.findByUserOwnerNickNameOrUserOwnerEmail(
            nickName = userGroup.userOwner.nickName,
            email = userGroup.userOwner.nickName
        ).let {
            assertThat(it).isNotNull
            assertThat(it?.userOwner).isNotNull
            assertThat(it?.userOwner?.nickName).isEqualTo(DEFAULT_NICK_NAME)
        }
    }

    @Test
    fun findUserGroupByUserId() {
        val userGroup = saveDefaultEntity()

        userGroupRepository.findByUserOwnerId(userGroup!!.userOwner!!.id!!).let {
            assertThat(it).isNotNull
            assertThat(it?.userOwner).isNotNull
            assertThat(it?.userOwner?.nickName).isEqualTo(DEFAULT_NICK_NAME)
        }
    }

    @Test
    fun giveUserGroupShouldCreateItAndThenCreateAnUser() {
        usrGroupCreated = userGroupService.save(createUserGroupDTO())

        assertThat(usrGroupCreated.id).isNotNull()
        assertThat(usrGroupCreated.name).isEqualTo(DEFAULT_NAME_USERGROUP)
        assertThat(usrGroupCreated.userOwner.id).isNotNull()
    }

    @Test
    fun givenUserGroupExistingThenShouldNotCreateAnotherGroup() {
        userGroupService.save(createUserGroupDTO())
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            userGroupService.save(createUserGroupDTO())
        }
    }

    @Test
    fun givenUserExistingThenShouldNotCreateUserAndThrowsAnException() {
        userServiceImpl.save(createUserDTO())
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            userServiceImpl.save(createUserDTO())
        }
    }

    @Test
    fun givenUserGroupNotSingleThenShouldBeCreated() {
        val singleGroupByUser = userGroupService.save(createUserGroupDTO())

        var userGroupNotSingle = singleGroupByUser.copy(id = null)
        userGroupNotSingle.single = false

        val usrGroupCreated = userGroupService.save(userGroupNotSingle)

        assertThat(singleGroupByUser.id).isNotEqualTo(usrGroupCreated.id)
    }

    @Test
    fun givenUserExistingThenSingleGroupShouldBeCreated() {
        val userCreated = userServiceImpl.save(createUserDTO())

        saveDefaultEntity(userOwner = userCreated).let {
            assertThat(it.id).isNotNull()
        }
    }

    override fun givenEntityAgainstConstraintsThenSaveItShouldThrowException() {
        saveDefaultEntity(name = "AA")
    }

    override fun givenNotExistingEntityFindByIdShouldReturnNull() {
        userGroupService.findById(UUID.randomUUID()).let {
            assertThat(it).isNull()
        }
    }

    @Ignore
    override fun givenEntityUpdateItShouldBeSuccessful() {
        saveDefaultEntity().let {
            val newGroupName = "Other Group Name"
            val notSingle = false
            val userOwnerModified = it.toDTO().userOwner
                .copy(
                    email = "sssss@test.com"
                )
            val userGroupUpdated = userGroupService.update(
                it.toDTO().copy(
                    name = newGroupName,
                    single = notSingle,
                    userOwner = userOwnerModified
                )
            )
            assertThat(userGroupUpdated).isNotNull
            assertThat(userGroupUpdated.single).isFalse()
            assertThat(userGroupUpdated.userOwner.id).isEqualTo(userOwnerModified.id)
        }
    }

    override fun givenEntityAgainstConstraintsThenUpdateItShouldThrowException() {
        saveDefaultEntity().let {
            userGroupService.update(it.toDTO().copy(name = "AA"))
        }
    }

    override fun giveEntityCreatedThenDeleteIt() {
        saveDefaultEntity().let {
            userGroupService.delete(it.toDTO())
        }
    }

    override fun givenEntityNotExistingThenDeleteItShouldThrowAnException() {
        userGroupService.delete(createUserGroupDTO(id = UUID.randomUUID()))
    }

    override fun givenEntityWithIdNullThenDeleteItShouldThrowAnException() {
        userGroupService.delete(createUserGroupDTO(id = null))
    }

    override fun givenNotUniqueEntityThenSaveItShouldThrowAnException() {
        saveDefaultEntity()
        saveDefaultEntity()
    }

    override fun givenNotUniqueEntityThenUpdateItShouldThrowAnException() {
        val singleGroupForDefaultUser = saveDefaultEntity()
        val defaultUserDTO = singleGroupForDefaultUser.userOwner.toDTO()
        val anotherUserDTO = createUserDTO(email = "anotherUser@gmail.com", nickName = "anotherUserXXX")
        saveDefaultEntity(userOwner = anotherUserDTO).let {
            val anotherUserUpdated = it.toDTO().copy(userOwner = anotherUserDTO.copy(email = defaultUserDTO.email))
            userGroupService.update(anotherUserUpdated)
        }
    }
}
