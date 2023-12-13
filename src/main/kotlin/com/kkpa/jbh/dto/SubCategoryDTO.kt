package com.kkpa.jbh.dto

import com.fasterxml.jackson.annotation.JsonIgnore
import com.kkpa.jbh.domain.categories.Category
import com.kkpa.jbh.domain.categories.SubCategories
import com.kkpa.jbh.domain.categories.SubCategoriesDefault
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.UUID

@Component
data class SubCategoryDTO(
    @JsonIgnore
    val id: UUID? = null,
    var name: String = "",
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
    val categoryId: UUID? = null,
    var subCategoryDefault: SubCategoriesDefault = SubCategoriesDefault.customSubCategoryDefault()
) : JbhDTO, DomainConverter<SubCategoryDTO, SubCategories> {

    override fun toDomain(): SubCategories {
        return SubCategories(
            id = this.id,
            name = this.name,
            category = Category(id = categoryId),
            subCategoryDefault = subCategoryDefault
        )
    }
}
