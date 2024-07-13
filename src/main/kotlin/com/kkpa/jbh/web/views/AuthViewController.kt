package com.kkpa.jbh.web.views

import com.kkpa.jbh.payload.SignUpRequest
import com.kkpa.jbh.web.service.AuthService
import org.slf4j.LoggerFactory
import org.springframework.http.MediaType
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/view/auth")
class AuthViewController(private val authService: AuthService) {

    private val log = LoggerFactory.getLogger(AuthViewController::class.java)

    @GetMapping("/login")
    fun loginPage(): String {
        // References to templates folder from resources folder
        return "auth/login"
    }

    @GetMapping("/register")
    fun registerPage(): String {
        return "auth/register"
    }

    @PostMapping("/register")
    fun registerUser(@ModelAttribute signUpRequest: SignUpRequest, model: Model): String {
        val userMsgResponse = "userMsgResponse";
        try {
            val result = authService.registerUser(signUpRequest)
            model.addAttribute("registrationResult", result)
            log.info("Registration result: $result")
            return "auth/register :: #registration-result"
        } catch (e: Exception) {
            log.error("Registration failed", e)
            model.addAttribute("registrationResult", "Registration failed: ${e.message}")
            return "auth/register :: #registration-result"
        }
    }

    @GetMapping("/check-username", produces = [MediaType.TEXT_HTML_VALUE])
    @ResponseBody
    fun checkUsername(@RequestParam username: String): String {
        val isAvailable = authService.checkUsernameAvailability(username)
        return if (isAvailable) {
            "<span style='color: green;'>Username is available</span>"
        } else {
            "<span style='color: red;'>Username is already taken</span>"
        }
    }

    @GetMapping("/check-email", produces = [MediaType.TEXT_HTML_VALUE])
    @ResponseBody
    fun checkEmail(@RequestParam email: String): String {
        val isAvailable = authService.checkEmailAvailability(email)
        return if (isAvailable) {
            "<span style='color: green;'>Email is available</span>"
        } else {
            "<span style='color: red;'>Email is already in use</span>"
        }
    }

}