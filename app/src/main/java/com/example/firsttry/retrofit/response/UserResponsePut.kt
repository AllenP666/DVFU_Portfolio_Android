package com.example.firsttry.retrofit.response

data class UserResponsePut(
    val access_token: String,
    val refresh_token: String,
    val expiration_access_token_in_seconds: Int
)
