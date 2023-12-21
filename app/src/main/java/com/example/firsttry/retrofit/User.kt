package com.example.firsttry.retrofit

data class User(
    val id: Int,
    val password: String,
    val last_login: String,
    val is_super_user: Boolean,
    val username: String,
    val first_name: String,
    val last_name: String,
    val email: String,
    val is_staff: Boolean,
    val if_active: Boolean,
    val date_joined: Boolean
    )