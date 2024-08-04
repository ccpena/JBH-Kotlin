package com.jbh.iam.core.model

import com.jbh.iam.common.config.DefaultValues
import java.util.*

open class UserGroupCore(
    val id: UUID? = null,
    val name: String = DefaultValues.EMPTY_STRING,
    val single: Boolean = true,
    val userOwner: UserCore = UserCore()
)

