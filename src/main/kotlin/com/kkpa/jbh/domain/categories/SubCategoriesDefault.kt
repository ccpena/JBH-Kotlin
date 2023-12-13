package com.kkpa.jbh.domain.categories

import com.kkpa.jbh.domain.AuditModel
import com.kkpa.jbh.util.DefaultValues
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

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