package com.kkpa.jbh.services.categories

import com.kkpa.jbh.domain.toDTOList
import com.kkpa.jbh.domain.toDTOSet
import com.kkpa.jbh.dto.CategoryDTO
import com.kkpa.jbh.dto.SubCategoryDTO
import com.kkpa.jbh.repositories.categories.SubCategoriesDefaultRepository
import com.kkpa.jbh.repositories.categories.SubCategoryRepository
import com.kkpa.jbh.services.CrudService
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class SubCategoryServiceImpl(
    private val subCategoryRepo: SubCategoryRepository,
    private val categoryService: CategoryServiceImpl,
    private val subCategoriesDefaultRepository: SubCategoriesDefaultRepository

) : CrudService<SubCategoryDTO>(), SubCategoryService {

    override fun validateSaveOperation(dto: SubCategoryDTO) {
        isNewAndUnique(dto)
    }

    override fun isNewAndUnique(dto: SubCategoryDTO): Boolean {
        if (dto.id == null) {
            isUnique(dto)
            return true
        }
        return false
    }

    private fun isUnique(subCategoryDTO: SubCategoryDTO): Boolean {
        if (subCategoryDTO.categoryId == null) throw java.lang.IllegalArgumentException("Category not found for subcategory ${subCategoryDTO.name}")

        val subCategoriesFromCategoryBD = findSubCategoriesByCategory(CategoryDTO(subCategoryDTO.categoryId))
        val subCatSameName = subCategoriesFromCategoryBD.stream().filter {
            it.name == subCategoryDTO.name
        }.count()

        if (subCatSameName != 0L) throw IllegalArgumentException("An subcategory already exists with the same nickName")

        return subCatSameName == 0L
    }

    override fun validateUpdateOperation(dto: SubCategoryDTO) {
        isUnique(dto)
    }

    override fun updateOperation(dto: SubCategoryDTO): SubCategoryDTO {
        return subCategoryRepo.save(dto.toDomain()).toDTO()
    }

    override fun delete(dto: SubCategoryDTO): Boolean {
        if (dto.id == null) {
            throw IllegalArgumentException("Not subcategory to be deleted")
        }

        try {
            subCategoryRepo.delete(dto.toDomain())
        } catch (enf: JpaObjectRetrievalFailureException) {
            throw IllegalArgumentException("SubCategory ${dto.id} not found")
        }

        return true
    }

    override fun findById(id: UUID): SubCategoryDTO? {
        val subCategory = subCategoryRepo.findById(id)

        return if (subCategory.isPresent) subCategory.get().toDTO() else null
    }

    override fun saveOperation(subCategoryDTO: SubCategoryDTO): SubCategoryDTO {
        return subCategoryRepo.save(subCategoryDTO.toDomain()).toDTO()
    }

    override fun findAll(): List<SubCategoryDTO> {
        val subcategories = subCategoryRepo.findAll()

        return subcategories.toDTOList()
    }

    fun findSubCategoriesByCategory(categoryDTO: CategoryDTO): Set<SubCategoryDTO> {
        val categoryBD = categoryDTO.toDomain()
        val subCategories = subCategoryRepo.findByCategory(categoryBD)
        return subCategories.toDTOSet()
    }
}
