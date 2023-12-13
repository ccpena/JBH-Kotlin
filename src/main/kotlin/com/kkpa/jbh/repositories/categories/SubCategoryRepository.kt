package com.kkpa.jbh.repositories.categories

import com.kkpa.jbh.domain.categories.Category
import com.kkpa.jbh.domain.categories.SubCategories
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface SubCategoryRepository : JpaRepository<SubCategories, UUID> {

    fun findByCategory(category: Category): Set<SubCategories>
}
