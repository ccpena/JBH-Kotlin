package com.kkpa.jbh.controllers.authentication

import com.kkpa.jbh.controllers.Routes
import com.kkpa.jbh.domain.Users.RoleName
import com.kkpa.jbh.domain.Users.User
import com.kkpa.jbh.dto.AccountsDTO
import com.kkpa.jbh.dto.UserGroupDTO
import com.kkpa.jbh.exceptions.web.AppException
import com.kkpa.jbh.payload.ApiResponse
import com.kkpa.jbh.payload.JwtAuthenticationResponse
import com.kkpa.jbh.payload.LoginRequest
import com.kkpa.jbh.payload.SignUpRequest
import com.kkpa.jbh.payload.UserIdentityAvailabilityResponse
import com.kkpa.jbh.repositories.users.RoleRepository
import com.kkpa.jbh.security.JwtTokenProvider
import com.kkpa.jbh.services.accounts.AccountServiceImpl
import com.kkpa.jbh.services.users.UserGroupServiceImpl
import com.kkpa.jbh.services.users.UserServiceImpl
import com.kkpa.jbh.trying.LockedEntities
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.util.Collections
import java.util.UUID


@RestController
@RequestMapping(Routes.AUTH_PATH)
class AuthController {

    @Autowired
    lateinit var authenticationManager: AuthenticationManager

    @Autowired
    lateinit var userGroupServiceImpl: UserGroupServiceImpl

    @Autowired
    lateinit var userService: UserServiceImpl

    @Autowired
    lateinit var roleRepository: RoleRepository

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    lateinit var tokenProvider: JwtTokenProvider

    @Autowired
    lateinit var accountServiceImpl: AccountServiceImpl

    companion object {
        private val log = LoggerFactory.getLogger(AuthController::class.java)
    }

    @PostMapping("/signin")
    fun authenticateUser( @RequestBody loginRequest: LoginRequest): ResponseEntity<JwtAuthenticationResponse> {

        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(
                loginRequest.usernameOrEmail,
                loginRequest.password!!.trim()
            )
        )

        SecurityContextHolder.getContext().authentication = authentication

        val jwt = tokenProvider.generateToken(authentication)
        return ResponseEntity.ok<JwtAuthenticationResponse>(JwtAuthenticationResponse(jwt))
    }

    @PostMapping("/signup")
    fun registerUser(@RequestBody signUpRequest: SignUpRequest): ResponseEntity<*> {
        if (userService.existsByNickName(signUpRequest.username)) {
            return ResponseEntity(
                ApiResponse(false, "NickName is already taken!"),
                HttpStatus.BAD_REQUEST
            )
        }

        if (userService.existsByEmail(signUpRequest.email)) {
            return ResponseEntity(
                ApiResponse(false, "Email Address already in use!"),
                HttpStatus.BAD_REQUEST
            )
        }

        // Creating user's accountId
        val user = User(
            userName = signUpRequest.name, nickName = signUpRequest.username,
            email = signUpRequest.email, password = signUpRequest.password
        )

        user.password = passwordEncoder.encode(user.password)

        val userRole = roleRepository.findByName(RoleName.ROLE_USER)
            .orElseThrow { AppException("User Role not set.") }

        user.roles = (Collections.singleton(userRole))

        val userGroupCreated = userGroupServiceImpl.save(UserGroupDTO(name = user.nickName, userOwner = user.toDTO()))

        // Saving default account
        val defaultAccountDTO = AccountsDTO(
            description = "Default Account for ${userGroupCreated.userOwner?.nickName}",
            userGroup = userGroupCreated
        )

        try {
            accountServiceImpl.save(defaultAccountDTO)
        }catch (e: Exception){
            log.error("Error creating the account", e)
            return ResponseEntity.badRequest().body<Any>(ApiResponse(false, "Error creating the account"))
        }


        val location = ServletUriComponentsBuilder
            .fromCurrentContextPath().path("/api/users/{username}")
            .buildAndExpand(userGroupCreated.userOwner?.nickName).toUri()

        return ResponseEntity.created(location).body<Any>(ApiResponse(true, "User registered successfully"))
    }


    @GetMapping("/checkUsernameAvailability")
    fun checkUsernameAvailability(@RequestParam(value = "nickName") nickName: String): ResponseEntity<UserIdentityAvailabilityResponse> {
        val isAvailable = !userService.existsByNickName(nickName)
        return ResponseEntity.ok(UserIdentityAvailabilityResponse(isAvailable))
    }

    @GetMapping("/checkEmailAvailability")
    fun checkEmailAvailability(@RequestParam(value = "email") email: String): ResponseEntity<UserIdentityAvailabilityResponse> {
        val isAvailable = !userService.existsByEmail(email)
        return ResponseEntity.ok(UserIdentityAvailabilityResponse(isAvailable))
    }
}