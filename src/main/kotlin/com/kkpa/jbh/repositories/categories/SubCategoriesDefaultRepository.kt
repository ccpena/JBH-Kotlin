package com.kkpa.jbh.repositories.categories

import com.kkpa.jbh.domain.categories.SubCategoriesDefault
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SubCategoriesDefaultRepository : JpaRepository<SubCategoriesDefault, Int>