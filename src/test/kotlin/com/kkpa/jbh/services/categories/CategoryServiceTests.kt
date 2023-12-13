package com.kkpa.jbh.services.categories

import com.kkpa.jbh.domain.Users.UsersGroup
import com.kkpa.jbh.domain.categories.CategoriesDefault
import com.kkpa.jbh.domain.categories.Category
import com.kkpa.jbh.domain.toDTOSet
import com.kkpa.jbh.dto.CategoryDTO
import com.kkpa.jbh.repositories.categories.CategoriesDefaultRepository
import com.kkpa.jbh.services.ServicesTests
import com.kkpa.jbh.services.users.UserServicesTests
import com.kkpa.jbh.services.users.UserServicesTests.Companion.createUserGroupDomain
import com.kkpa.jbh.util.DefaultValues
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import java.util.UUID

class CategoryServiceTests : ServicesTests() {

    @Autowired
    lateinit var categoryService: CategoryServiceImpl

    @Autowired
    lateinit var subCategoryServiceImpl: SubCategoryServiceImpl

    companion object {
        private val log = LoggerFactory.getLogger(CategoryServiceTests::class.java)
        val DEFAULT_CATEGORY_NAME = "Category By Default - Test Mode"

        fun createDefaultCategoryEntity(
            categoryId: UUID? = null,
            name: String = DEFAULT_CATEGORY_NAME,
            usersGroup: UsersGroup = UsersGroup()
        ): Category {
            return Category(
                id = categoryId,
                name = name,
                usersGroup = usersGroup,
                categoryDefault = getCustomCategoryDefault()
            )
        }

        private val categoryDefaultRepository by lazy {
            if (context == null) throw IllegalAccessException("Context not found")
            context!!.getBean(CategoriesDefaultRepository::class.java)
        }

        private val categoryService by lazy {
            if (context == null) throw IllegalAccessException("Context not found")
            context!!.getBean(CategoryServiceImpl::class.java)
        }

        fun getCustomCategoryDefault(): CategoriesDefault {
            return categoryDefaultRepository.findById(DefaultValues.DEFAULT_CUSTOM_ID).orElseGet {
                categoryDefaultRepository.save(CategoriesDefault.customCategory())
            }
        }

        fun getCustomDefaultCategoryCreatedDTO(): CategoryDTO {
            return categoryService.findOne(DEFAULT_CATEGORY_NAME) ?: saveDefaultEntity().toDTO()
        }

        fun saveDefaultEntity(): Category {
            return categoryService.save(createDefaultCategoryEntity(usersGroup = createUserGroupDomain()).toDTO())
                .toDomain()
        }

        /**
         * This just will work to tests marked as @Test from jupiter
         */
        @BeforeAll
        @JvmStatic
        internal fun beforeAll() {
            log.info("Removing categories created previously")
            categoryService.findAll().forEach {
                categoryService.delete(it)
            }
            log.info("Categories BD: ${categoryService.findAll().size}")
        }
    }

    fun saveDefaultEntity(): Category {
        return categoryService.save(createDefaultCategoryEntity(usersGroup = UserServicesTests.createUserGroupDomain()).toDTO())
            .toDomain()
    }

    @Test
    override fun givenEntityThenShouldBeCreated() {
        val categoryCreated = getCustomDefaultCategoryCreatedDTO()
        val subCategoriesFromCategoryCreated = subCategoryServiceImpl.findSubCategoriesByCategory(categoryCreated)

        assertThat(categoryCreated.id).isNotNull()
        assert(categoryCreated.name == DEFAULT_CATEGORY_NAME)
        assertThat(subCategoriesFromCategoryCreated.size).isEqualTo(0)
    }

    override fun givenEntityAgainstConstraintsThenSaveItShouldThrowException() {
        categoryService.save(Category().toDTO())
    }

    override fun givenNotExistingEntityFindByIdShouldReturnNull() {
        val notExistingCategory = createDefaultCategoryEntity(categoryId = UUID.randomUUID())
        assertThat(categoryService.findById(notExistingCategory.id!!)).isNull()
    }

    override fun givenEntityUpdateItShouldBeSuccessful() {
        val categoryCreated = getCustomDefaultCategoryCreatedDTO()

        val categoryName = "Category Updated"
        categoryCreated.copy(name = categoryName).let {
            categoryService.update(it)

            val categoryFound = categoryService.findById(it.id!!)

            assertThat(categoryFound).isNotNull

            categoryFound?.let { assertThat(it.name).isEqualTo(categoryName) }
        }
    }

    override fun givenEntityAgainstConstraintsThenUpdateItShouldThrowException() {
        val categoryCreated = getCustomDefaultCategoryCreatedDTO()

        val categoryName = "##"
        categoryCreated.copy(name = categoryName).let {
            categoryService.update(it)
        }
    }

    override fun giveEntityCreatedThenDeleteIt() {
        assertThat(categoryService.delete(getCustomDefaultCategoryCreatedDTO())).isTrue()
    }

    override fun givenEntityNotExistingThenDeleteItShouldThrowAnException() {
        createDefaultCategoryEntity(categoryId = UUID.randomUUID()).also {
            categoryService.delete(it.toDTO())
        }
    }

    override fun givenEntityWithIdNullThenDeleteItShouldThrowAnException() {
        createDefaultCategoryEntity(categoryId = null).also {
            categoryService.delete(it.toDTO())
        }
    }

    override fun givenNotUniqueEntityThenUpdateItShouldThrowAnException() {
        val categoryBD = getCustomDefaultCategoryCreatedDTO().toDomain()

        val anotherCatNotEqualNameSameUserDTO = createDefaultCategoryEntity(
            categoryId = null,
            name = "Second Category For Same User",
            usersGroup = categoryBD.usersGroup.copy()
        ).toDTO()
        val anotherCatNotEqualNameSameUser = categoryService.save(anotherCatNotEqualNameSameUserDTO)

        assertThat(anotherCatNotEqualNameSameUser).isNotNull

        anotherCatNotEqualNameSameUser.name = categoryBD.name
        categoryService.update(anotherCatNotEqualNameSameUser)
    }

    @Test
    fun givenSubCategoriesListThenCategoryShouldBeCreated() {
        var categoryToCreatedDTO = getCustomDefaultCategoryCreatedDTO()

        val categoryToCreate = categoryToCreatedDTO.toDomain()
        val subCategories = SubCategoriesServiceTests.createSubCategoriesEntities(categoryToCreate)

        val subCategoriesDTO = subCategories.toDTOSet()
        categoryToCreatedDTO.subCategories = subCategoriesDTO

        val categoryCreated = categoryService.save(categoryToCreatedDTO)

        val subCategoriesFromCategoryCreated = subCategoryServiceImpl.findSubCategoriesByCategory(categoryCreated)

        assertThat(categoryCreated.id).isNotNull()
        assertThat(subCategoriesFromCategoryCreated.size).isEqualTo(subCategories.size)
    }

    @Test
    fun givenSubCategoriesWithCategoryStoredThenCategoryShouldBeUpdated() {
        var categoryToCreatedDTO = getCustomDefaultCategoryCreatedDTO()

        val categoryToCreate = (categoryToCreatedDTO.toDomain())
        val subCategories = SubCategoriesServiceTests.createSubCategoriesEntities(categoryToCreate)

        val subCategoriesDTO = subCategories.toDTOSet()
        categoryToCreatedDTO.subCategories = subCategoriesDTO
        val categoryDTOCreated = categoryService.update(categoryToCreatedDTO)

        val categoryCreated = categoryService.findById(categoryDTOCreated.id!!)

        assertThat(categoryCreated).isNotNull
        categoryCreated?.let {
            val subCategoriesFromCategoryCreated = subCategoryServiceImpl.findSubCategoriesByCategory(categoryCreated)
            println(subCategoriesFromCategoryCreated)
            assertThat(categoryCreated.id).isNotNull()
            assertThat(subCategoriesFromCategoryCreated.size).isEqualTo(subCategories.size)
        }
    }

    override fun givenNotUniqueEntityThenSaveItShouldThrowAnException() {
        categoryService.save(getCustomDefaultCategoryCreatedDTO().copy(id = null))
    }
}