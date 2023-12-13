package com.kkpa.jbh.services.categories

import com.kkpa.jbh.dto.UserGroupCategoryDTO
import com.kkpa.jbh.services.CrudService
import java.util.UUID

class UserGroupCategoryServiceImpl : CrudService<UserGroupCategoryDTO>(), UserGroupCategoryService {

    override fun validateSaveOperation(dto: UserGroupCategoryDTO) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun validateUpdateOperation(dto: UserGroupCategoryDTO) {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun saveOperation(dto: UserGroupCategoryDTO): UserGroupCategoryDTO {
        TODO("not implemented") // To change body of created functions use File  | Settings | File Templates.
    }

    override fun updateOperation(dto: UserGroupCategoryDTO): UserGroupCategoryDTO {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(dto: UserGroupCategoryDTO): Boolean {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun findAll(): List<UserGroupCategoryDTO> {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun findById(id: UUID): UserGroupCategoryDTO? {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }

    override fun isNewAndUnique(dto: UserGroupCategoryDTO): Boolean {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }
}
