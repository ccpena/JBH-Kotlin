package com.kkpa.jbh.dto

import com.kkpa.jbh.domain.Users.UsersGroup
import java.math.BigDecimal
import java.time.LocalDate

data class OnlineShoppingDTO(
    val articuleName: String,
    val usersGroup: UsersGroup,
    val effectiveDate: LocalDate = LocalDate.now(),
    val usdPrice: BigDecimal = BigDecimal.ONE,
    val zoneIdPrice: BigDecimal = BigDecimal.ONE,
    val dollarRate: BigDecimal = BigDecimal.ONE,
    val shippingWeight: BigDecimal = BigDecimal.ONE,
    val shippingPrice: BigDecimal = BigDecimal.ONE,
    val grandTotal: BigDecimal = BigDecimal.ONE
)