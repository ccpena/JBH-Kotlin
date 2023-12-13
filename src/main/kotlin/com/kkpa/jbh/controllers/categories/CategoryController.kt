package com.kkpa.jbh.controllers.categories

import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider
import com.kkpa.jbh.controllers.Routes
import com.kkpa.jbh.dto.CategoryDTO
import com.kkpa.jbh.exceptions.categories.CategoryNotFoundException
import com.kkpa.jbh.services.categories.CategoryServiceImpl
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.json.MappingJacksonValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI
import java.util.UUID

@RestController
@RequestMapping(Routes.CATEGORIES_PATH)
class CategoryController(val categoryService: CategoryServiceImpl) {

    @GetMapping
    fun getCategory(): MappingJacksonValue {
        val categoryDTO = CategoryDTO(id = UUID.randomUUID(), name = "Category Name")

        val filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "byDefault")

        val filters = SimpleFilterProvider().addFilter("categoryFilter", filter)

        val mapping = MappingJacksonValue(categoryDTO)

        mapping.filters = filters

        return mapping
    }

    @GetMapping("/dentity")
    fun getCategoryEntity(): ResponseEntity<MappingJacksonValue> {
        val categoryDTO = CategoryDTO(id = UUID.randomUUID(), name = "Category Name")

        val filter = SimpleBeanPropertyFilter.filterOutAllExcept("id", "byDefault")

        val filters = SimpleFilterProvider().addFilter("categoryFilter", filter)
        filters.setFailOnUnknownId(false)

        val wrapper = MappingJacksonValue(categoryDTO)
        wrapper.filters = filters
        return ResponseEntity.ok(wrapper)
    }

    @GetMapping("/sinfilter")
    fun getCategoryity(): CategoryDTO {
        return CategoryDTO(id = UUID.randomUUID(), name = "Category Name")
    }

    @GetMapping(Routes.GET_CATEGORY_BY_NAME_URI)
    fun getCategoryByName(@PathVariable name: String): ResponseEntity<CategoryDTO> {
        val categoryDTO: CategoryDTO? = categoryService.findOne(name)

        categoryDTO?.apply {
            return ResponseEntity.ok(this)
        }

        throw CategoryNotFoundException("The category $name doesn't exists")
    }

    @PostMapping(Routes.ROOT_PATH)
    fun createCategory(@RequestBody categoryDTO: CategoryDTO): ResponseEntity<CategoryDTO> {
        val newCategory: CategoryDTO

        newCategory = categoryService.save(categoryDTO)

        val location: URI = ServletUriComponentsBuilder.fromCurrentRequest().path("/{nickName}")
            .buildAndExpand(newCategory.name).toUri()

        return ResponseEntity.created(location).body(newCategory)
    }
}
