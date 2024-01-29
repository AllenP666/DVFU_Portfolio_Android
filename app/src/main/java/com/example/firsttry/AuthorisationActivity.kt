package com.example.firsttry

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.firsttry.retrofit.UserAPI
import com.example.firsttry.retrofit.requests.UserAuthRequestPost
import com.example.firsttry.retrofit.response.UserResponsePost
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthorisationActivity : Activity() {
    fun executeUserAuth(userAuthRequestPost: UserAuthRequestPost, context: Context) {
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

        userAPI.auth_user(userAuthRequestPost).enqueue(object: Callback<UserResponsePost> {
            override fun onResponse(
                call: Call<UserResponsePost>,
                response: Response<UserResponsePost>
            ) {
                if (response.isSuccessful) {
                    val intent = Intent(context, CodeAuthActivationActivity::class.java)
                    intent.putExtra("tfa-token", response.body()?.tfa_token.toString())
                    startActivity(intent)
                } else {
                    Toast.makeText(context, "Ошибка при авторизации пользователя", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserResponsePost>, t: Throwable) {
                Toast.makeText(context, "Ошибка при авторизации пользователя", Toast.LENGTH_SHORT).show()
            }
        })
    }

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

            if (login == "" || password == ""){
                Toast.makeText(this, "Ошибка при авторизации пользователя", Toast.LENGTH_SHORT).show()
            }
            else {
                val context = this
                executeUserAuth(UserAuthRequestPost(login, password), context)
            }
        }
    }
}