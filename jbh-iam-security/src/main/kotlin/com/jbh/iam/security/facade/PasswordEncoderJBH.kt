package com.jbh.iam.security.facade

import com.jbh.iam.core.security.IPasswordEncoder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class PasswordEncoderJBH : IPasswordEncoder {
    @Autowired
    lateinit var passwordEncoder: BCryptPasswordEncoder


    override fun encode(password: String): String {
        return passwordEncoder.encode(password)
    }

}