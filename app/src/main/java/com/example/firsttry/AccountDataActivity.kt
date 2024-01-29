package com.example.firsttry

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firsttry.retrofit.UserAPI
import com.example.firsttry.retrofit.response.AchievementResponse
import com.example.firsttry.retrofit.response.UserResponceGet
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AccountDataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_data)

        val exitBtn: Button = findViewById(R.id.exitBtn)
        val addAchBtn: Button = findViewById(R.id.button_add_achievement)
        val refreshBtn: Button = findViewById(R.id.button_refresh)
        val userName: TextView = findViewById(R.id.userName)
        val sharedPreferences = getSharedPreferences("INFO", Context.MODE_PRIVATE)
        val achievementsList: RecyclerView = findViewById(R.id.achievementsList)
        val achievements = arrayListOf<Achievements>() // Наш список достижений

        var first_name = ""
        var second_name = ""
        var third_name = ""
        var phone_number = ""
        var email = ""
        var login = "Base Name"

        achievementsList.layoutManager = LinearLayoutManager(this)
        achievementsList.adapter = AchievementsAdapter(achievements, this)

        addAchBtn.setOnClickListener {
            val intent = Intent(this, AddAchievementActivity::class.java)
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

        userAPI.get_user_achievements(tfaToken).enqueue(object: Callback<List<AchievementResponse>> {
            override fun onResponse(
                call: Call<List<AchievementResponse>>,
                response: Response<List<AchievementResponse>>
            ) {
                if (response.isSuccessful) {
                    val listAchievements = response.body()
                    if (listAchievements != null) {
                        for (i in listAchievements)
                            achievements.add(Achievements(i.title, i.description, i.score, i.id.toString()))
                    }
                    achievementsList.layoutManager = LinearLayoutManager(applicationContext)
                    achievementsList.adapter = AchievementsAdapter(achievements, applicationContext)
                } else {
                    Toast.makeText(applicationContext, "Ошибка при обновлении достижений", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<AchievementResponse>>, t: Throwable) {
                Toast.makeText(applicationContext, "Ошибка при обновлении достижений", Toast.LENGTH_SHORT).show()
            }
        })

        refreshBtn.setOnClickListener {
            userAPI.get_user_achievements(tfaToken).enqueue(object: Callback<List<AchievementResponse>> {
                override fun onResponse(
                    call: Call<List<AchievementResponse>>,
                    response: Response<List<AchievementResponse>>
                ) {
                    if (response.isSuccessful) {
                        val listAchievements = response.body()
                        achievements.clear()
                        if (listAchievements != null) {
                            for (i in listAchievements)
                                achievements.add(Achievements(i.title, i.description, i.score, i.id.toString()))
                        }
                        achievementsList.layoutManager = LinearLayoutManager(applicationContext)
                        achievementsList.adapter = AchievementsAdapter(achievements, applicationContext)
                    } else {
                        Toast.makeText(applicationContext, "Ошибка при обновлении достижений", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<List<AchievementResponse>>, t: Throwable) {
                    Toast.makeText(applicationContext, "Ошибка при обновлении достижений", Toast.LENGTH_SHORT).show()
                }
            })
        }

        userAPI.get_user_info(tfaToken).enqueue(object: Callback<UserResponceGet> {
            override fun onResponse(
                call: Call<UserResponceGet>,
                response: Response<UserResponceGet>
            ) {
                if (response.isSuccessful) {
                    first_name = response.body()?.first_name.toString()
                    second_name = response.body()?.second_name.toString()
                    third_name = response.body()?.third_name.toString()
                    phone_number = response.body()?.phone_number.toString()
                    email = response.body()?.email.toString()
                    login = response.body()?.login.toString()

                    userName.text = "$second_name $first_name $third_name"
                } else {
                    Toast.makeText(applicationContext, "Ошибка при получении данных пользователя", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserResponceGet>, t: Throwable) {
                Toast.makeText(applicationContext, "Ошибка при получении данных пользователя", Toast.LENGTH_SHORT).show()
            }
        })
    }
}