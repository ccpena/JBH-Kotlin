package com.kkpa.jbh.payload

import javax.validation.constraints.NotBlank

class LoginRequest {
    @NotBlank
    var usernameOrEmail: String? = null

    @NotBlank
    var password: String? = null
}