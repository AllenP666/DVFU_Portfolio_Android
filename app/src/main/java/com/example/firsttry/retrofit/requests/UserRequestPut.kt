package com.example.firsttry.retrofit.requests

data class UserRequestPut(
    val tfa_token: String,
    val confirm_code: String
)
