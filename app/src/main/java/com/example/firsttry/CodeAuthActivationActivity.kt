package com.example.firsttry

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.firsttry.retrofit.UserAPI
import com.example.firsttry.retrofit.requests.UserRequestPut
import com.example.firsttry.retrofit.response.UserResponsePut
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CodeAuthActivationActivity : AppCompatActivity() {
    fun executeCodeReg(userRequestPut: UserRequestPut, context: Context) {
        val sharedPreferences = getSharedPreferences("INFO", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
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

        userAPI.auth_user_code(userRequestPut).enqueue(object: Callback<UserResponsePut> {
            override fun onResponse(
                call: Call<UserResponsePut>,
                response: Response<UserResponsePut>
            ) {
                if (response.isSuccessful) {
                    editor.putString("access_token", response.body()?.access_token.toString())
                    editor.putString("refresh_token", response.body()?.refresh_token.toString())
                    editor.apply()
                    val intent = Intent(context, AccountDataActivity::class.java)
                    startActivity(intent)

                } else {
                    Toast.makeText(context, "Неверный код", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UserResponsePut>, t: Throwable) {
                Toast.makeText(context, "Неверный код", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_code_activation)
        val buttonBack: Button = findViewById(R.id.code_button_back)
        val codeText: EditText = findViewById(R.id.code_edittext)
        val buttonConfirm: Button = findViewById(R.id.code_button)

        buttonBack.setOnClickListener{
            val intent = Intent(this, AuthorisationActivity::class.java)
            startActivity(intent)
        }

        val tfaToken = intent.getStringExtra("tfa-token").toString()

        buttonConfirm.setOnClickListener{
            val text = codeText.text.toString().trim()
            executeCodeReg(UserRequestPut(tfaToken, text), this)
        }
    }
}