package com.kkpa.jbh.payload

import java.util.UUID

data class UserSummaryResponse(
    val id: UUID,
    val username: String,
    val name: String
)