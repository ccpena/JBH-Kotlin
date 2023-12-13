package com.kkpa.jbh.util

import com.kkpa.jbh.JbhApplicationConfigTests
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.junit4.SpringRunner

@SpringBootTest
@RunWith(SpringRunner::class)
@Import(JbhApplicationConfigTests::class)
class PasswordEncoderTest {

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Test
    fun checkEncodeSamePassword() {
        val password = "12345678"
        val encoded = passwordEncoder.encode(password)
        println(encoded)

        assertThat(passwordEncoder.matches(password, encoded)).isTrue()
    }
}