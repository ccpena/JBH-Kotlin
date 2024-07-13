package com.kkpa.jbh.web.service

import com.kkpa.jbh.payload.SignUpRequest
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.servlet.support.ServletUriComponentsBuilder



@Service
class AuthService(private val restTemplate: RestTemplate) {

    fun registerUser(signUpRequest: SignUpRequest): String {
        val baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()
        val response = restTemplate.postForEntity(baseUrl + "/auth/signup", signUpRequest, String::class.java)
        return response.body ?: "Error occurred during registration"
    }

    fun checkUsernameAvailability(username: String): Boolean {
        val baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()
        val response = restTemplate.getForEntity(baseUrl + "/auth/checkUsernameAvailability?nickName=$username", Map::class.java)
        return response.body?.get("available") as Boolean? ?: false
    }

    fun checkEmailAvailability(email: String): Boolean {
        val baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()
        val response = restTemplate.getForEntity(baseUrl +"/auth/checkEmailAvailability?email=$email", Map::class.java)
        return response.body?.get("available") as Boolean? ?: false
    }
}