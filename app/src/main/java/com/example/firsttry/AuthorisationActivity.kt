package com.example.firsttry

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class AuthorisationActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorisation)

        // Заставочный экран
        installSplashScreen().setKeepOnScreenCondition {
            false
        }

        val userLogin: EditText = findViewById(R.id.user_login_auth)
        val userPassword: EditText = findViewById(R.id.user_password_auth)
        val buttonAuth: Button = findViewById(R.id.button_auth)
        val linkToReg: TextView = findViewById(R.id.link_to_reg)
        val rememberMe: CheckBox = findViewById(R.id.rememberMe)
        val sharedPreferences = getSharedPreferences("INFO", Context.MODE_PRIVATE)
        val checkBox = sharedPreferences.getString("rememberMe", "")

        // Запомнить меня
        if (checkBox.equals("True")) {
            val intent = Intent(this, AccountDataActivity::class.java)
            startActivity(intent)
        }

        // TextView, при нажатии на него, переходит на окно регистрации
        linkToReg.setOnClickListener{
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }

        // Кнопка для регистрации
        buttonAuth.setOnClickListener {
            val login = userLogin.text.toString().trim()
            val password = userPassword.text.toString().trim()
            val db = DbHelper(this, null)

            // Поиск пользователя по паролю и логину
            // Если есть:
            if (db.getUser(login, password)){
                Toast.makeText(this, "Вход выполнен", Toast.LENGTH_SHORT).show()

                // Сохранение данных пользователя при успешной регистрации
                val editor = sharedPreferences.edit()
                editor.putString("login", login)
                editor.putString("password", password)
                editor.apply()

                val intent = Intent(this, AccountDataActivity::class.java)
                userLogin.text.clear()
                userPassword.text.clear()
                startActivity(intent)
            }
            // Если нет:
            else {
                Toast.makeText(this, "Неверно введены данные", Toast.LENGTH_SHORT).show()
            }
        }

        rememberMe.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                val editor = sharedPreferences.edit()
                editor.putString("rememberMe", "True")
                editor.apply()
            }else{
                val editor = sharedPreferences.edit()
                editor.putString("rememberMe", "False")
                editor.apply()
            }
        }
    }
}