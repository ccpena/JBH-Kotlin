package com.kkpa.jbh.apis

import com.kkpa.jbh.controllers.Routes
import com.kkpa.jbh.controllers.categories.CategoryController
import com.kkpa.jbh.domain.categories.Category
import com.kkpa.jbh.dto.CategoryDTO
import com.kkpa.jbh.extensions.convertToByteArray
import com.kkpa.jbh.repositories.categories.CategoryRepository
import com.kkpa.jbh.services.categories.CategoryServiceImpl
import com.kkpa.jbh.services.categories.CategoryServiceTests
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.context.ApplicationContext
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@RunWith(
    SpringRunner::class
)
@ActiveProfiles("test")
@WebMvcTest(CategoryController::class, secure = false)
@Ignore
class CategoryApiTests {

    @Autowired lateinit var mockMvc: MockMvc

    lateinit var categoryService: CategoryServiceImpl

    @Autowired
    lateinit var categoryRepository: CategoryRepository

    @Autowired
    private lateinit var appContext: ApplicationContext

    val DEFAULT_NAME: String = "My Default Category"

    fun createEntity(): Category {
        return Category(name = DEFAULT_NAME)
    }

    @Before
    fun setup() {
    }

    @Test
    fun givenACategoryShouldBeCreated() {

        val categoryDTO = CategoryServiceTests.getCustomDefaultCategoryCreatedDTO()

        val dbSizeBeforeCreate: Int = categoryService.findAll().size

        mockMvc.perform(
            post(Routes.CATEGORIES_PATH + Routes.ROOT_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(categoryDTO.convertToByteArray()!!)
        ).andExpect(status().isCreated)

        val categories: List<CategoryDTO> = categoryService.findAll()

        assertThat(categories).hasSize(dbSizeBeforeCreate + 1)
        val categoryCreated: CategoryDTO = categories.get(dbSizeBeforeCreate)
        assertThat(categoryCreated.name).isEqualTo(DEFAULT_NAME)
    }

    @Test
    fun givenANameGetACategory() {
        val categoryCreated = createEntity()

        mockMvc.perform(get(Routes.CATEGORIES_PATH + Routes.ROOT_PATH)).andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.nickName").value(categoryCreated.name))
    }
}
