package com.jbh.iam.core.access

import org.springframework.stereotype.Component

@Component
interface AuthenticationOperation {
    fun authenticate(usernameOrEmail: String, password: String): String
}