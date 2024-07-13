package com.kkpa.jbh.web.service

import com.kkpa.jbh.payload.SignUpRequest
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class AuthService(private val restTemplate: RestTemplate) {

    fun registerUser(signUpRequest: SignUpRequest): String {
        val response = restTemplate.postForEntity("/api/auth/signup", signUpRequest, String::class.java)
        return response.body ?: "Error occurred during registration"
    }

    fun checkUsernameAvailability(username: String): Boolean {
        val response = restTemplate.getForEntity("/api/auth/checkUsernameAvailability?nickName=$username", Map::class.java)
        return response.body?.get("available") as Boolean? ?: false
    }

    fun checkEmailAvailability(email: String): Boolean {
        val response = restTemplate.getForEntity("/api/auth/checkEmailAvailability?email=$email", Map::class.java)
        return response.body?.get("available") as Boolean? ?: false
    }
}