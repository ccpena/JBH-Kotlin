package com.jbh.iam.security.facade

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class PasswordEncoderJBH {
    @Autowired
    lateinit var passwordEncoder: PasswordEncoder


    fun encode(password: String): String {
        return passwordEncoder.encode(password)
    }

}