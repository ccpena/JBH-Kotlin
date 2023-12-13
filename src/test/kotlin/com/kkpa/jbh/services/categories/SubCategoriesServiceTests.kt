package com.kkpa.jbh.services.categories

import com.kkpa.jbh.domain.categories.CUSTOM_CATEGORY_ID
import com.kkpa.jbh.domain.categories.Category
import com.kkpa.jbh.domain.categories.SubCategories
import com.kkpa.jbh.domain.categories.SubCategoriesDefault
import com.kkpa.jbh.dto.CategoryDTO
import com.kkpa.jbh.dto.UserGroupDTO
import com.kkpa.jbh.repositories.categories.SubCategoriesDefaultRepository
import com.kkpa.jbh.repositories.categories.SubCategoryRepository
import com.kkpa.jbh.services.ServicesTests
import com.kkpa.jbh.services.categories.CategoryServiceTests.Companion.createDefaultCategoryEntity
import com.kkpa.jbh.services.categories.CategoryServiceTests.Companion.getCustomDefaultCategoryCreatedDTO
import com.kkpa.jbh.services.users.UserGroupServiceImpl
import com.kkpa.jbh.services.users.UserServicesTests.Companion.createUserGroupDomain
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import java.util.UUID

class SubCategoriesServiceTests : ServicesTests() {

    @Autowired
    lateinit var subCategoryService: SubCategoryServiceImpl

    @Autowired
    lateinit var userGroupService: UserGroupServiceImpl

    @Autowired
    lateinit var categoryServiceImpl: CategoryServiceImpl

    @Autowired
    lateinit var subCategoriesDefaultRepository: SubCategoriesDefaultRepository

    companion object {
        const val DEFAULT_SUB_CATEGORY_NAME: String = "SUB_CATEGORY_NAME"
        const val DEFAULT_TOTAL_SUBCATEGORIES = 5

        private val subCategoriesDefaultRepository: SubCategoriesDefaultRepository by lazy {
            if (context == null) throw IllegalArgumentException("Context not found")
            context!!.getBean(SubCategoriesDefaultRepository::class.java)
        }

        private val subCategoryRepository: SubCategoryRepository by lazy {
            if (context == null) throw IllegalAccessException("Context not found")
            context!!.getBean(SubCategoryRepository::class.java)
        }

        fun getCustomSubCategoryDefault(): SubCategoriesDefault {
            return subCategoriesDefaultRepository.findById(CUSTOM_CATEGORY_ID).orElseGet {
                subCategoriesDefaultRepository.save(SubCategoriesDefault.customSubCategoryDefault())
            }
        }

        fun createSubCategoriesEntities(categoryCreated: Category): Set<SubCategories> {
            val subCategories = mutableSetOf<SubCategories>()

            for (i in 1..DEFAULT_TOTAL_SUBCATEGORIES) {
                val subCategory = SubCategories(
                    name = DEFAULT_SUB_CATEGORY_NAME + i,
                    category = categoryCreated,
                    subCategoryDefault = getCustomSubCategoryDefault()
                )
                subCategories.add(subCategory)
            }

            return subCategories.toSet()
        }

        fun createDefaultSubCategory(
            subCategoryId: UUID? = null,
            name: String = DEFAULT_SUB_CATEGORY_NAME,
            categoryDTOCreated: CategoryDTO? = null
        ): SubCategories {
            return SubCategories(
                id = subCategoryId,
                name = name,
                category = categoryDTOCreated?.toDomain() ?: getCustomDefaultCategoryCreatedDTO().toDomain(),
                subCategoryDefault = getCustomSubCategoryDefault()
            )
        }

        fun saveAndGetDefaultSubCategoryDomain(): SubCategories {
            return subCategoryRepository.save(createDefaultSubCategory())
        }
    }

    fun saveDefaultCategoryDTO(): CategoryDTO {
        return categoryServiceImpl.save(createDefaultCategoryEntity(usersGroup = createUserGroupDomain()).toDTO())
    }

    fun saveDefaultEntity(name: String = DEFAULT_SUB_CATEGORY_NAME): SubCategories {
        val subCategoryToCreated = createDefaultSubCategory(name = name)
        return subCategoryService.save(subCategoryToCreated.toDTO()).toDomain()
    }

    override fun givenEntityThenShouldBeCreated() {
        val subCategoryCreated = saveDefaultEntity()

        assertThat(subCategoryCreated).isNotNull
        assertThat(subCategoryCreated.id).isNotNull()
        assertThat(subCategoryCreated.name).isEqualTo(DEFAULT_SUB_CATEGORY_NAME)
        assertThat(subCategoryCreated.category!!).isNotNull
        assertThat(subCategoryCreated.category!!.id).isNotNull()
    }

    override fun givenEntityAgainstConstraintsThenSaveItShouldThrowException() {
        val subCategory = SubCategories(
            name = "     ",
            category = saveDefaultCategoryDTO().toDomain()
        )
        subCategoryService.save(subCategory.toDTO())
    }

    override fun givenNotExistingEntityFindByIdShouldReturnNull() {

        subCategoryService.findById(UUID.randomUUID()).let {
            assertThat(it).isNull()
        }
    }

    override fun givenEntityUpdateItShouldBeSuccessful() {
        val subCategoryCreated = saveDefaultEntity()
        val newName = "Category Updated"
        val subCategoryToUpdate = subCategoryCreated.copy(name = newName)
        subCategoryService.update(subCategoryToUpdate.toDTO()).let {
            assertThat(it.updatedAt).isAfter(subCategoryCreated.updatedAt)
            assertThat(it.name).isEqualTo(newName)
        }
    }

    override fun givenEntityAgainstConstraintsThenUpdateItShouldThrowException() {
        val subCategoryCreated = saveDefaultEntity()
        val newName = "Ca"
        val subCategoryToUpdate = subCategoryCreated.copy(name = newName)
        subCategoryService.update(subCategoryToUpdate.toDTO())
    }

    override fun giveEntityCreatedThenDeleteIt() {
        val subCategoryCreated = saveDefaultEntity()
        subCategoryService.delete(subCategoryCreated.toDTO()).let {
            assertThat(it).isTrue()
        }
    }

    override fun givenEntityNotExistingThenDeleteItShouldThrowAnException() {
        createDefaultSubCategory(
            subCategoryId = UUID.randomUUID(),
            categoryDTOCreated = CategoryDTO(id = UUID.randomUUID())
        ).let {
            subCategoryService.delete(it.toDTO())
        }
    }

    override fun givenEntityWithIdNullThenDeleteItShouldThrowAnException() {
        createDefaultSubCategory(subCategoryId = null).let {
            subCategoryService.delete(it.toDTO())
        }
    }

    override fun givenNotUniqueEntityThenSaveItShouldThrowAnException() {
        saveDefaultEntity()
        saveDefaultEntity()
    }

    override fun givenNotUniqueEntityThenUpdateItShouldThrowAnException() {
        saveDefaultEntity()
        val subCategoryToBeUpdated = saveDefaultEntity(name = "SubCategoryToBeUpdated")
        subCategoryToBeUpdated.let {
            val subCateUpdated = subCategoryService.findById(it.id!!)
            subCateUpdated?.let {
                it.name = DEFAULT_SUB_CATEGORY_NAME
                subCategoryService.update(it)
            }
        }
    }

    @Test
    fun givenCategoryExistingSubCategShouldBeCreatedAndNotCreateCategory() {
        val subCategoriesDefault = SubCategoriesDefault.customSubCategoryDefault()

        val categoryDTOExisting = getCustomDefaultCategoryCreatedDTO()

        val subCategoryToCreated = SubCategories(
            name = DEFAULT_SUB_CATEGORY_NAME,
            category = categoryDTOExisting.toDomain()
        )

        val subCategoryCreatedDTO = subCategoryService.save(subCategoryToCreated.toDTO())

        assertThat(subCategoryCreatedDTO.id).isNotNull()
        assertThat(subCategoryCreatedDTO.categoryId).isNotNull()
        assertThat(subCategoryCreatedDTO.categoryId).isEqualTo(categoryDTOExisting.id)
        assertThat(subCategoryCreatedDTO.name).isEqualTo(DEFAULT_SUB_CATEGORY_NAME)
        assertThat(subCategoryCreatedDTO.subCategoryDefault).isEqualTo(subCategoriesDefault)
    }

    @Test(expected = IllegalArgumentException::class)
    fun givenSubCategoryWithoutCategoryThenShouldThrowAnException() {
        createDefaultSubCategory(categoryDTOCreated = CategoryDTO(usersGroupDTO = UserGroupDTO())).let {
            subCategoryService.save(it.toDTO())
        }
    }
}
