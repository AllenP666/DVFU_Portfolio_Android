package com.example.firsttry

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.example.firsttry.retrofit.AuthRequest
import com.example.firsttry.retrofit.UserAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class AccountDataActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_data)

        val exitBtn: Button = findViewById(R.id.exitBtn)
//        val userName: TextView = findViewById(R.id.userName)
        val sharedPreferences = getSharedPreferences("INFO", Context.MODE_PRIVATE)
        val newsList: RecyclerView = findViewById(R.id.newsList)
        val news = arrayListOf<News>() // Наш список новостей

        news.add(News("Подсчет баллов для портфолио", "Вы подали документы на подсчет баллов.", sharedPreferences.getString("login","").toString(), 0, 0, false))
        news.add(News("Подсчет баллов для портфолио", "Вы подали документы на подсчет баллов.", sharedPreferences.getString("login","").toString(), 20, 1, true))

        newsList.layoutManager = LinearLayoutManager(this)
        newsList.adapter = NewsAdapter(news, this)


//        val retrofit = Retrofit.Builder()
//            .baseUrl("http://127.0.0.1:8000/")
//            .addConverterFactory(GsonConverterFactory.create()).build()
//        val userAPI = retrofit.create(UserAPI::class.java)
//
//        val username = sharedPreferences.getString("login", "")
//        val password = sharedPreferences.getString("password", "")
//
//        CoroutineScope(Dispatchers.IO).launch {
//            val user = userAPI.auth(
//                AuthRequest(
//                    username.toString(),
//                    password.toString()
//                )
//            )
//            runOnUiThread {
//                userName.text = "Добрый день ${user.username}"
//            }
//        }


        // Кнопка выхода: возвращает на оконо авторизации, но прежде вызывает AlertDialog, для предотвращения случайных нажатий
        exitBtn.setOnClickListener{
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder
                .setMessage("Вы точно хотите выйти?")
                .setPositiveButton("Да") { dialog, which ->
                    // Удаление данных пользователя при выходе
                    val editor = sharedPreferences.edit()
                    editor.putString("login", "")
                    editor.putString("password", "")
                    editor.putString("rememberMe", "False")
                    editor.apply()

                    val intent = Intent(this, AuthorisationActivity::class.java)
                    startActivity(intent)
                }
                .setNegativeButton("Нет") { dialog, which ->
                    dialog.dismiss()
                }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }
}