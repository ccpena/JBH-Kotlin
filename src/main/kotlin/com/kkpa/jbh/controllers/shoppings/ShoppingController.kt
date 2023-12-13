package com.kkpa.jbh.controllers.shoppings

import com.kkpa.jbh.controllers.Routes
import com.kkpa.jbh.dto.OnlineShoppingDTO
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(Routes.SHOPPING_PATH)
class ShoppingController {

    @PostMapping
    fun createShopping(@RequestBody shoppingRequest: OnlineShoppingDTO) {
    }
}