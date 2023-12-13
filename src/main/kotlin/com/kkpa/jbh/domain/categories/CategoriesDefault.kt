package com.kkpa.jbh.domain.categories

import com.kkpa.jbh.domain.AuditModel
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

const val CUSTOM_CATEGORY_ID = -1
const val CUSTOM_CATEGORY_NAME = "Custom Category"

/**
 * Custom categories are categories created by one user.
 * There are some default categories that will be shared between all users
 * in order to track by categories all the movements of the users.
 */
@Entity
@Table(name = "categories_default")
data class CategoriesDefault(
    @Id
    val id: Int,

    @Column(name = "name")
    val name: String

) : AuditModel() {

    companion object {

        fun customCategory(): CategoriesDefault {
            return CategoriesDefault(CUSTOM_CATEGORY_ID, CUSTOM_CATEGORY_NAME)
        }
    }
}