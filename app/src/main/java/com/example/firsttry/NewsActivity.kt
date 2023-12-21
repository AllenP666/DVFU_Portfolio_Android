package com.example.firsttry

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class NewsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        val exitBtn: Button = findViewById(R.id.exitBtn_2)
        val goHome: ImageView = findViewById(R.id.MyUniver)
        val sharedPreferences = getSharedPreferences("INFO", Context.MODE_PRIVATE)

        val header: TextView = findViewById(R.id.news_activity_header)
        val description: TextView = findViewById(R.id.news_activity_description)
        val points: TextView = findViewById(R.id.news_activity_points)

        val userName = intent.getStringExtra("userName")
        val userPoints = intent.getStringExtra("pointSum")
        val userHeader = intent.getStringExtra("header")
        val userDescription = intent.getStringExtra("description")
        val userId = intent.getStringExtra("id")
        val userReadiness = intent.getStringExtra("readiness")


        // Заполнение окна данными в зависимости от статуса выполнения
        var execution = ""
        var accrual = ""
        var isPoints = ""
        if (userReadiness == "true"){
            execution = "рассмотрено"
            accrual = "Для получения большей информации перейдите по ссылке: (ссылка)"
            isPoints = "Колличество баллов"
        }
        else{
            execution = "не рассмотрено"
            accrual = "В скором времени ваша заявка будет рассмотрена и мы пришлем вам уведомление."
            isPoints = "Предварительное колличество баллов"
        }

        header.text = userHeader
        description.text = "$userName, ваше заявление под номером: $userId, $execution. $accrual"
        points.text = "$isPoints: $userPoints"


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