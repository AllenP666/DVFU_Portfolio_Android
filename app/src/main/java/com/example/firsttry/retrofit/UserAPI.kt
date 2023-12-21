package com.example.firsttry.retrofit

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserAPI {
    @GET("/users/{id}")
    suspend fun getUserById(@Path("id") id: Int): User

    @POST("users/login")
    suspend fun auth(@Body authRequest: AuthRequest): User
}