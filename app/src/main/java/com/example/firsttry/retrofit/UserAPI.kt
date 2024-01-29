package com.example.firsttry.retrofit

import com.example.firsttry.retrofit.requests.AchievementRequestDelete
import com.example.firsttry.retrofit.requests.AchievementRequestPost
import com.example.firsttry.retrofit.requests.UserAuthRequestPost
import com.example.firsttry.retrofit.requests.UserRegRequestPost
import com.example.firsttry.retrofit.requests.UserRequestPut
import com.example.firsttry.retrofit.response.AchievementResponse
import com.example.firsttry.retrofit.response.UserResponceGet
import com.example.firsttry.retrofit.response.UserResponsePost
import com.example.firsttry.retrofit.response.UserResponsePut
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT

interface UserAPI {
    // POST
    @POST("auth/registration/")
    fun register_user(@Body userRegRequest: UserRegRequestPost): Call<UserResponsePost>

    @POST("auth/")
    fun auth_user(@Body userAuthRequestPost: UserAuthRequestPost): Call<UserResponsePost>

    @POST("api/v1/achievements/")
    fun add_achievement(@Header("Authorization") access_token: String,
                        @Body achievementRequestPost: AchievementRequestPost): Call<AchievementResponse>

    // PUT
    @PUT("auth/registration/")
    fun register_user_code(@Body userRequestPut: UserRequestPut): Call<UserResponsePut>

    @PUT("auth/")
    fun auth_user_code(@Body userRequestPut: UserRequestPut): Call<UserResponsePut>

    // GET
    @GET("api/v1/user_info/")
    fun get_user_info(@Header("Authorization") access_token: String): Call<UserResponceGet>

    @GET("api/v1/achievements/")
    fun get_user_achievements(@Header("Authorization") access_token: String): Call<List<AchievementResponse>>

    // DELETE
    @HTTP(method = "DELETE", path = "api/v1/achievements/", hasBody = true)
    fun delete_user_achievement(@Header("Authorization") access_token: String,
                                @Body achievementRequestDelete: AchievementRequestDelete): Call<AchievementResponse>
}