package com.kkpa.jbh.repositories.categories

import com.kkpa.jbh.domain.categories.Category
import org.intellij.lang.annotations.Language
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface CategoryRepository : JpaRepository<Category, UUID> {

    fun findByName(name: String): Category?

    @Language(value = "JPAQL")
    @Query(
        value = """
                SELECT c FROM Category c WHERE
                c.name = :name
                AND c.usersGroup.id = :userGroupId
                """
    )
    fun findByNameAndUserGroup(@Param("name") name: String, @Param("userGroupId") usersGroupId: UUID): Category?
}
