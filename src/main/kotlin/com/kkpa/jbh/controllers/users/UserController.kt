package com.kkpa.jbh.controllers.users

import com.kkpa.jbh.controllers.Routes
import com.kkpa.jbh.dto.UserDTO
import com.kkpa.jbh.payload.UserProfileResponse
import com.kkpa.jbh.payload.UserSummaryResponse
import com.kkpa.jbh.security.CurrentUser
import com.kkpa.jbh.security.UserPrincipal
import com.kkpa.jbh.services.users.UserServiceImpl
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.ZoneOffset

@RestController
@RequestMapping(Routes.USERS_PATH)
class UserController(private val userServiceImpl: UserServiceImpl) {

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    fun getMe(@CurrentUser currentUser: UserPrincipal): ResponseEntity<UserSummaryResponse> {
        val userSummaryResponse =
            UserSummaryResponse(currentUser.id!!, username = currentUser.username, name = currentUser.name)
        return ResponseEntity.ok(userSummaryResponse)
    }

    @GetMapping("/{nickName}")
    fun getUserProfile(@PathVariable(value = "nickName") nickName: String): ResponseEntity<UserProfileResponse> {
        val user = userServiceImpl.findByNickName(nickName)

        val userProfile = UserProfileResponse(
            name = user.userName, id = user.id!!, username = user.nickName, joinedAt = user.createdAt!!.toInstant(
                ZoneOffset.UTC
            )
        )

        return ResponseEntity.ok(userProfile)
    }

    @GetMapping("/")
    fun findAll(): List<UserDTO> = userServiceImpl.findAll()
}