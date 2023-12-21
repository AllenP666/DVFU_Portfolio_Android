package com.example.firsttry

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast


class RegistrationActivity : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val userLogin: EditText = findViewById(R.id.user_login)
        val userPassword: EditText = findViewById(R.id.user_password)
        val buttonReg: Button = findViewById(R.id.button_reg)
        val linkToAuth: TextView = findViewById(R.id.link_to_auth)

        // TextView, при нажатии на него, переходит на окно авторизации
        linkToAuth.setOnClickListener{
            val intent = Intent(this, AuthorisationActivity::class.java)
            startActivity(intent)
        }

        // Кнопка регистрации пользователя
        buttonReg.setOnClickListener {
            val login = userLogin.text.toString().trim()
            val password = userPassword.text.toString().trim()

            // Проверка на пустые поля
            if (login == "" || password == "")
                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_SHORT).show()
            else {
                val user = User(login, password)
                val db = DbHelper(this, null)

                // Проверка на существование пользователя в базе данных
                if (db.getLogin(login))
                    Toast.makeText(this, "Такой пользователь уже существует", Toast.LENGTH_SHORT).show()

                // Добавление пользователя в базу данных
                else{
                    db.addUser(user)
                    Toast.makeText(this, "Пользователь $login добавлен", Toast.LENGTH_SHORT).show()

                    userLogin.text.clear()
                    userPassword.text.clear()
                }
            }
        }
    }
}