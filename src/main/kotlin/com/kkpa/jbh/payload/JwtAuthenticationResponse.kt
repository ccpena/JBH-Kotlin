package com.kkpa.jbh.payload

class JwtAuthenticationResponse(var accessToken: String?) {
    var tokenType = "Bearer"
}