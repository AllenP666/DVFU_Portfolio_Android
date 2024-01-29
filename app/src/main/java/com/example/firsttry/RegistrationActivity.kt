package com.example.firsttry

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.firsttry.retrofit.UserAPI
import com.example.firsttry.retrofit.requests.UserRegRequestPost
import com.example.firsttry.retrofit.response.UserResponsePost
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory


class RegistrationActivity : AppCompatActivity()
{
    fun executeUserReg(userRegRequestPost: UserRegRequestPost, context: Context) {
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

        userAPI.register_user(userRegRequestPost).enqueue(object: Callback<UserResponsePost>{
            override fun onResponse(
                call: Call<UserResponsePost>,
                response: Response<UserResponsePost>
            ) {
                if (response.isSuccessful) {
                    val intent = Intent(context, CodeRegActivationActivity::class.java)
                    intent.putExtra("tfa-token", response.body()?.tfa_token.toString())
                    startActivity(intent)
                } else {
                    Toast.makeText(context, "Ошибка при регистрации пользователя", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserResponsePost>, t: Throwable) {
                Toast.makeText(context, "Ошибка при регистрации пользователя", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        val userLogin: EditText = findViewById(R.id.reg_login)
        val userPassword: EditText = findViewById(R.id.user_password)
        val userPasswordRe: EditText = findViewById(R.id.user_password_re)
        val userFirstName: EditText = findViewById(R.id.user_first_name)
        val userSecondName: EditText = findViewById(R.id.user_second_name)
        val userThirdName: EditText = findViewById(R.id.user_third_name)
        val userPhone: EditText = findViewById(R.id.phone_number)
        val userEmail: EditText = findViewById(R.id.reg_email)
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
            val re_password = userPasswordRe.text.toString().trim()
            val first_name = userFirstName.text.toString().trim()
            val second_name = userSecondName.text.toString().trim()
            val third_name = userThirdName.text.toString().trim()
            val phone = userPhone.text.toString().trim()
            val email = userEmail.text.toString().trim()

            // Проверка на пустые поля
            if (login == "" || password == "" || first_name == "" || second_name == ""
                || third_name == "" || phone == "" || email == "" || re_password == "")
                Toast.makeText(this, "Не все поля заполнены", Toast.LENGTH_SHORT).show()

            else {
                if (password != re_password)
                    Toast.makeText(this, "Пароли не совпадают", Toast.LENGTH_SHORT).show()

                else {
                    val context = this
                    executeUserReg(UserRegRequestPost(
                        login,
                        first_name,
                        second_name,
                        third_name,
                        email,
                        phone,
                        password,
                        re_password), context)
                }
            }
        }
    }
}