package com.kkpa.jbh.services.categories

import com.kkpa.jbh.domain.categories.Category
import com.kkpa.jbh.domain.toDTOList
import com.kkpa.jbh.dto.CategoryDTO
import com.kkpa.jbh.repositories.categories.CategoryRepository
import com.kkpa.jbh.services.CrudService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
@Transactional
class CategoryServiceImpl(
    private val categoryRepo: CategoryRepository
) : CrudService<CategoryDTO>(), CategoryService {

    companion object {
        private val log = LoggerFactory.getLogger(CategoryServiceImpl::class.java)
    }

    override fun validateSaveOperation(dto: CategoryDTO) {
        if (isNotUniqueByNameAndUserGroup(dto)) throw IllegalArgumentException("A category with same nickName is already registered for the user and it cannot be saved")
    }

    override fun validateUpdateOperation(dto: CategoryDTO) {
        if (isNotUniqueByNameAndUserGroup(dto)) throw IllegalArgumentException("A category with same nickName is already registered for the user, and it cannot be updated")
    }

    override fun updateOperation(categoryDTO: CategoryDTO): CategoryDTO {
        categoryRepo.findById(categoryDTO.id!!)
            .orElseThrow { IllegalArgumentException("Category not found ${categoryDTO.id}, and it can not be updated.") }

        return categoryRepo.save(categoryDTO.toDomain()).toDTO()
    }

    override fun delete(dto: CategoryDTO): Boolean {
        dto?.also {
            try {
                categoryRepo.deleteById(dto.id!!); return true
            } catch (enf: Exception) {
                throw IllegalArgumentException("Category $it not found")
            }
        }

        throw IllegalArgumentException("Not category to be deleted")
    }

    override fun isNewAndUnique(dto: CategoryDTO): Boolean {
        return dto.id == null || !isNotUniqueByNameAndUserGroup(dto)
    }

    private fun isNotUniqueByNameAndUserGroup(categoryDTO: CategoryDTO): Boolean {
        categoryRepo.findByNameAndUserGroup(categoryDTO.name, categoryDTO.usersGroupDTO.id!!)?.let {
            return categoryDTO.id != it.id
        }
        return false
    }

    override fun findById(id: UUID): CategoryDTO? = categoryRepo.findById(id).orElse(null).let { it?.toDTO() }

    override fun saveOperation(categoryDTO: CategoryDTO): CategoryDTO {
        var categoryCreated: Category
        var categoryDTOCreated = CategoryDTO(null)

        var categoryBDToCreate = categoryDTO.toDomain()

        if (isNewAndUnique(categoryDTO)) {

            categoryCreated = categoryRepo.save(categoryBDToCreate)
            categoryDTOCreated = categoryCreated.toDTO()
            return categoryDTOCreated
        }
        return categoryDTOCreated
    }

    override fun findAll(): List<CategoryDTO> {
        val categories: List<Category> = categoryRepo.findAll().toList()
        return categories.toDTOList()
    }

    fun findOne(name: String): CategoryDTO? {

        val category = categoryRepo.findByName(name)

        category?.apply {
            return category.toDTO()
        }

        return null
    }
}
