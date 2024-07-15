package com.jbh.iam.viewgateway.web.service


import com.jbh.iam.common.config.JBHConstants.JBH_TOKEN_COOKIE_NAME
import com.jbh.iam.common.payload.LoginRequest
import com.jbh.iam.common.payload.SignUpRequest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.servlet.support.ServletUriComponentsBuilder


@Service
class AuthService(private val restTemplate: RestTemplate) {

    private val AUTH_API_PATH = "/auth";

    companion object {
        private val log = LoggerFactory.getLogger(AuthService::class.java)
    }

    fun loginUser(loginRequest: LoginRequest): Pair<String, String> {
        val baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()
        val response = restTemplate.postForEntity(baseUrl + AUTH_API_PATH + "/signin", loginRequest, String::class.java)

        // Extract the Cookies from the response
        val jwtCookie = response.headers["Set-Cookie"]?.find { it.startsWith("$JBH_TOKEN_COOKIE_NAME=") }
        response.body?.let { log.info("Login response: $it") }

        return Pair("OK", jwtCookie ?: "")
    }

    fun registerUser(signUpRequest: SignUpRequest): String {
        val baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()
        val response = restTemplate.postForEntity(baseUrl + "/auth/signup", signUpRequest, String::class.java)
        return response.body ?: "Error occurred during registration"
    }

    fun checkUsernameAvailability(username: String): Boolean {
        val baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()
        val response =
            restTemplate.getForEntity(baseUrl + "/auth/checkUsernameAvailability?nickName=$username", Map::class.java)
        return response.body?.get("available") as Boolean? ?: false
    }

    fun checkEmailAvailability(email: String): Boolean {
        val baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString()
        val response = restTemplate.getForEntity(baseUrl + "/auth/checkEmailAvailability?email=$email", Map::class.java)
        return response.body?.get("available") as Boolean? ?: false
    }
}