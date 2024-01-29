package com.example.firsttry.retrofit.requests

data class UserRegRequestPost(
    val login: String,
    val first_name: String,
    val second_name: String,
    val third_name: String,
    val email: String,
    val phone: String,
    val password: String,
    val re_password: String
    )