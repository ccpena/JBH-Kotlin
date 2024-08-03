package com.jbh.iam.core.security

import org.springframework.stereotype.Component

@Component
interface IPasswordEncoder {
    fun encode(password: String): String
}