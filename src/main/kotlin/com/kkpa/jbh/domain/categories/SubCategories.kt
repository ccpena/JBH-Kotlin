package com.kkpa.jbh.domain.categories

import com.kkpa.jbh.domain.AuditModel
import com.kkpa.jbh.domain.DTOConverter
import com.kkpa.jbh.dto.SubCategoryDTO
import com.kkpa.jbh.util.DefaultValues.MAX_DESC_NAMES_LENGTH
import com.kkpa.jbh.util.DefaultValues.MIN_DESC_NAMES_LENGTH
import java.util.UUID
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Entity
data class SubCategories(

    @org.hibernate.annotations.GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "UUID")
    @Id
    val id: UUID? = null,

    @Column(name = "NAME")
    @get:Size(
        min = MIN_DESC_NAMES_LENGTH,
        message = "Name should have at least $MIN_DESC_NAMES_LENGTH and maximum $MAX_DESC_NAMES_LENGTH characters",
        max = MAX_DESC_NAMES_LENGTH
    )
    @get:NotBlank(message = "Blank spaces are not allowed")
    val name: String = "",

    @OneToOne
    @JoinColumn(name = "sub_category_default_id", referencedColumnName = "id")
    val subCategoryDefault: SubCategoriesDefault = SubCategoriesDefault.customSubCategoryDefault(),

    @ManyToOne(fetch = FetchType.LAZY, cascade = arrayOf(CascadeType.REFRESH))
    @JoinColumn(name = "category_id")
    var category: Category? = null

) : AuditModel(), DTOConverter<SubCategoryDTO, SubCategories> {

    override fun toDTO(): SubCategoryDTO {
        return SubCategoryDTO(
            id = this.id,
            name = this.name,
            categoryId = category?.id ?: null,
            subCategoryDefault = subCategoryDefault,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
    }
}
