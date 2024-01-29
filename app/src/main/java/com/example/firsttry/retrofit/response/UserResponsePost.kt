package com.example.firsttry.retrofit.response

data class UserResponsePost(
    val tfa_token: String,
    val expiration_confirm_code_in_seconds: Int
)
