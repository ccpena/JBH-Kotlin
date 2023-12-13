package com.kkpa.jbh.dto

import com.fasterxml.jackson.annotation.JsonFilter
import com.fasterxml.jackson.annotation.JsonProperty
import com.kkpa.jbh.domain.categories.CategoriesDefault
import com.kkpa.jbh.domain.categories.Category
import org.springframework.context.annotation.Scope
import org.springframework.stereotype.Component
import java.util.UUID

@Component
@Scope("prototype")
@JsonFilter("categoryFilter")
data class CategoryDTO(
    @JsonProperty("id")
    val id: UUID? = null,
    var name: String = "",
    val usersGroupDTO: UserGroupDTO = UserGroupDTO(),
    var categoryDefault: CategoriesDefault = CategoriesDefault.customCategory(),
    var subCategories: Set<SubCategoryDTO> = mutableSetOf()
) : JbhDTO, DomainConverter<CategoryDTO, Category> {

    override fun toDomain(): Category {
        val categoryBD = Category(
            id = this.id,
            name = this.name,
            categoryDefault = categoryDefault,
            usersGroup = usersGroupDTO.toDomain()
        )
        subCategories.forEach {
            categoryBD.addSubCategory(it.toDomain())
        }
        return categoryBD
    }
}
