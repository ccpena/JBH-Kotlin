package com.kkpa.jbh.domain.categories

import com.kkpa.jbh.domain.AuditModel
import com.kkpa.jbh.domain.DTOConverter
import com.kkpa.jbh.domain.Users.UsersGroup
import com.kkpa.jbh.domain.toDTOSet
import com.kkpa.jbh.dto.CategoryDTO
import com.kkpa.jbh.util.DefaultValues
import com.kkpa.jbh.util.DefaultValues.MAX_DESC_NAMES_LENGTH
import com.kkpa.jbh.util.DefaultValues.MIN_DESC_NAMES_LENGTH
import java.util.Objects
import java.util.UUID
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Entity
@Table(name = "categories")
data class Category(
    @org.hibernate.annotations.GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "UUID")
    @Id
    val id: UUID? = null,

    @Column(name = "name", nullable = false, length = 50)
    @get:NotBlank(message = "Name is required")
    @get:Size(
        min = MIN_DESC_NAMES_LENGTH,
        message = "Name should have at least $MIN_DESC_NAMES_LENGTH and maximum $MAX_DESC_NAMES_LENGTH characters",
        max = MAX_DESC_NAMES_LENGTH
    )
    val name: String = DefaultValues.EMPTY_STRING,

    @OneToOne(cascade = [(CascadeType.ALL)])
    @JoinColumn(name = "category_default_id", referencedColumnName = "id")
    val categoryDefault: CategoriesDefault = CategoriesDefault.customCategory(),

    @ManyToOne()
    @JoinColumn(name = "users_group_id")
    val usersGroup: UsersGroup = UsersGroup(),

    @OneToMany(mappedBy = "category", cascade = arrayOf(CascadeType.ALL), orphanRemoval = true, fetch = FetchType.LAZY)
    val subCategories: MutableSet<SubCategories> = mutableSetOf<SubCategories>()

) : DTOConverter<CategoryDTO, Category>, AuditModel() {

    fun addSubCategory(subCategory: SubCategories) {
        subCategory.category = this
        subCategories.add(subCategory)
    }

    fun removeSubCategory(subCategory: SubCategories) {
        subCategories.remove(subCategory)
    }

    override fun hashCode(): Int {
        return Objects.hash(id, name)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other)
            return true
        if (other !is Category)
            return false

        return name == other.name
    }

    override fun toDTO(): CategoryDTO {
        return CategoryDTO(
            this.id,
            this.name,
            usersGroupDTO = usersGroup.toDTO(),
            categoryDefault = categoryDefault,
            subCategories = subCategories.toDTOSet()
        )
    }
}
