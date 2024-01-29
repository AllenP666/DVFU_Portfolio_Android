package com.example.firsttry

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firsttry.retrofit.UserAPI
import com.example.firsttry.retrofit.requests.AchievementRequestDelete
import com.example.firsttry.retrofit.requests.AchievementRequestPost
import com.example.firsttry.retrofit.response.AchievementResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AchievementsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        val exitBtn: Button = findViewById(R.id.exitBtn_2)
        val goHome: ImageView = findViewById(R.id.MyUniver)
        val sharedPreferences = getSharedPreferences("INFO", Context.MODE_PRIVATE)
        val deleteBtn: Button = findViewById(R.id.deleteBtn)

        val titleV: TextView = findViewById(R.id.news_activity_header)
        val descriptionV: TextView = findViewById(R.id.news_activity_description)
        val pointsV: TextView = findViewById(R.id.news_activity_points)

        val id = intent.getStringExtra("id").toString()
        val title = intent.getStringExtra("title").toString()
        val description = intent.getStringExtra("description").toString()
        val points = intent.getStringExtra("points").toString()

        titleV.text = title
        descriptionV.text = description
        pointsV.text = "Ваше колличество баллов: $points"

        // Переход на главную страницу приложения
        goHome.setOnClickListener{
            val intent = Intent(this, AccountDataActivity::class.java)
            startActivity(intent)
        }

        // Кнопка выхода: возвращает на оконо авторизации, но прежде вызывает AlertDialog, для предотвращения случайных нажатий
        exitBtn.setOnClickListener{
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder
                .setMessage("Вы точно хотите выйти?")
                .setPositiveButton("Да") { dialog, which ->
                    val intent = Intent(this, AuthorisationActivity::class.java)
                    startActivity(intent)
                }
                .setNegativeButton("Нет") { dialog, which ->
                    dialog.dismiss()
                }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

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

        deleteBtn.setOnClickListener{
            userAPI.delete_user_achievement(tfaToken, AchievementRequestDelete(
                id
            ))
                .enqueue(object:
                Callback<AchievementResponse> {
                override fun onResponse(
                    call: Call<AchievementResponse>,
                    response: Response<AchievementResponse>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(applicationContext, "Достижения $title удалено!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(applicationContext, "Ошибка при удалении достижения $title", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<AchievementResponse>, t: Throwable) {
                    Toast.makeText(applicationContext, "Ошибка при удалении достижения $title", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}