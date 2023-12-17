package com.kkpa.jbh.domain.categories

import com.kkpa.jbh.domain.AuditModel
import com.kkpa.jbh.util.DefaultValues
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

const val SUB_CATEGORY_DEFAULT_NAME = "CUSTOM SUBCATEGORY FROM CUSTOM CATEGORY"

@Entity
@Table(name = "sub_categories_default")
data class SubCategoriesDefault(
    @Id
    val id: Int,

    @ManyToOne(cascade = arrayOf(CascadeType.ALL))
    @JoinColumn(name = "categoryDefaultId", referencedColumnName = "id")
    val categoryDefault: CategoriesDefault = CategoriesDefault.customCategory(),

    @Column(name = "name")
    val name: String
) : AuditModel() {

    companion object {
        fun customSubCategoryDefault(): SubCategoriesDefault {
            return SubCategoriesDefault(id = DefaultValues.DEFAULT_CUSTOM_ID, name = SUB_CATEGORY_DEFAULT_NAME)
        }
    }
}