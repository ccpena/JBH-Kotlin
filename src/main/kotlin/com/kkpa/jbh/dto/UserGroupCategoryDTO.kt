package com.kkpa.jbh.dto

import org.springframework.stereotype.Component

@Component
data class UserGroupCategoryDTO(

    val userGroup: UserGroupDTO = UserGroupDTO(),

    val categoryDTO: CategoryDTO = CategoryDTO()

) : JbhDTO