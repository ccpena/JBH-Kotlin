package com.kkpa.jbh.domain.shoppings

import com.kkpa.jbh.domain.AuditModel
import com.kkpa.jbh.domain.Users.UsersGroup
import com.kkpa.jbh.util.DefaultValues
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import javax.validation.constraints.Digits
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "online_shopping")
data class OnlineShopping(

    @org.hibernate.annotations.GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @GeneratedValue(generator = "UUID")
    @javax.persistence.Id
    private val id: UUID? = null,

    @Column(name = "articule_name")
    @field:NotEmpty(message = "Articule name is required")
    @field:Size(
        min = DefaultValues.MIN_DESC_NAMES_LENGTH,
        message = "Description should have at least ${DefaultValues.MIN_DESC_NAMES_LENGTH} characters",
        max = 50
    )
    val articuleName: String,

    @Column(name = "effective_date", nullable = false)
    @field:NotNull(message = "Effective date can be null")
    val effectiveDate: LocalDate? = null,

    @Column(name = "usd_price")
    @field:Digits(integer = 10, fraction = 2, message = "USD price is not correct.")
    val usdPrice: BigDecimal,

    @Column(name = "cop_price")
    @field:Digits(integer = 10, fraction = 2, message = "Local price is not correct.")
    val zoneIdPrice: BigDecimal,

    @Column(name = "dollar_rate")
    @field:Digits(integer = 6, fraction = 2, message = "Dollar rate not correct.")
    val dollarRate: BigDecimal? = null,

    @Column(name = "shipping_weight")
    @field:Digits(integer = 5, fraction = 2, message = "Shipping weight is not correct.")
    val shippingWeight: BigDecimal? = null,

    @Column(name = "shipping_price")
    @field:Digits(integer = 10, fraction = 2, message = "Shipping price is not correct.")
    val shippingPrice: BigDecimal? = null,

    @Column(name = "grand_total")
    @field:Digits(integer = 16, fraction = 2, message = "Grand Total is not correct.")
    val grandTotal: BigDecimal,

    @ManyToOne
    @JoinColumn(name = "user_group_id")
    val userGroup: UsersGroup

) : AuditModel()