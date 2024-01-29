package com.example.firsttry

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.firsttry.retrofit.UserAPI
import com.example.firsttry.retrofit.requests.AchievementRequestPost
import com.example.firsttry.retrofit.response.AchievementResponse
import com.example.firsttry.retrofit.response.UserResponceGet
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddAchievementActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_achievement)

        val backBtn: Button = findViewById(R.id.backBtn)
        val addAchBtn: Button = findViewById(R.id.addAchBtn)
        val title: EditText = findViewById(R.id.achievement_title)
        val description: EditText = findViewById(R.id.achievement_description)
        val points: EditText = findViewById(R.id.achievement_points)

        val sharedPreferences = getSharedPreferences("INFO", Context.MODE_PRIVATE)

        backBtn.setOnClickListener{
            val intent = Intent(this, AccountDataActivity::class.java)
            startActivity(intent)
        }

        addAchBtn.setOnClickListener {
            if (title.text.toString() == "" || description.text.toString() == "" || points.text.toString() == "") {
                Toast.makeText(applicationContext, "Заполните все поля!", Toast.LENGTH_SHORT).show()
            } else {
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY

                val client = OkHttpClient.Builder()
                    .addInterceptor(interceptor)
                    .build()

                val retrofit = Retrofit.Builder()
                    .baseUrl("http://89.111.170.194:4000/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val userAPI = retrofit.create(UserAPI::class.java)

                val tfaToken = sharedPreferences.getString("access_token", "").toString()

                userAPI.add_achievement(tfaToken, AchievementRequestPost(
                    title.text.toString(),
                    points.text.toString(),
                    description.text.toString()))
                    .enqueue(object: Callback<AchievementResponse> {
                        override fun onResponse(
                            call: Call<AchievementResponse>,
                            response: Response<AchievementResponse>
                        ) {
                            if (response.isSuccessful) {
                                Toast.makeText(applicationContext, "Достижение успешно создано!", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(applicationContext, "Ошибка при создании достижения", Toast.LENGTH_SHORT).show()
                            }
                        }
                        override fun onFailure(call: Call<AchievementResponse>, t: Throwable) {
                            Toast.makeText(applicationContext, "Ошибка при создании достижения", Toast.LENGTH_SHORT).show()
                        }
                    })
            }
        }
    }
}