package com.kkpa.jbh.services.shoppings

import com.kkpa.jbh.domain.Users.UsersGroup
import com.kkpa.jbh.domain.shoppings.OnlineShopping
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import java.math.BigDecimal
import java.time.LocalDate

@RunWith(SpringRunner::class)
@SpringBootTest
class OnlineShoppingServiceTest {

    companion object {
        fun getShoppingFrom(
            articuleName: String,
            usersGroup: UsersGroup,
            effectiveDate: LocalDate = LocalDate.now(),
            usdPrice: BigDecimal = BigDecimal.ONE,
            zoneIdPrice: BigDecimal = BigDecimal.ONE,
            dollarRate: BigDecimal = BigDecimal.ONE,
            shippingWeight: BigDecimal = BigDecimal.ONE,
            shippingPrice: BigDecimal = BigDecimal.ONE,
            grandTotal: BigDecimal = BigDecimal.ONE
        ): OnlineShopping {
            return OnlineShopping(
                articuleName = articuleName,
                effectiveDate = effectiveDate,
                usdPrice = usdPrice,
                zoneIdPrice = zoneIdPrice,
                dollarRate = dollarRate,
                shippingPrice = shippingPrice,
                shippingWeight = shippingWeight,
                grandTotal = grandTotal,
                userGroup = usersGroup
            )
        }
    }

    @Test
    fun givenShoppingThenItShouldBeSaved() {
    }
}