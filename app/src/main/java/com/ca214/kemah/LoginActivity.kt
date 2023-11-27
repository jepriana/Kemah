package com.ca214.kemah

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.DnsResolver.Callback
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.ca214.kemah.data.api.ApiConfig
import com.ca214.kemah.data.models.LoginRequest
import com.ca214.kemah.data.models.ResponseLogin
import retrofit2.Call
import retrofit2.Response

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    // Deklarasi object yang terhubung dengan layout
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button
    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText

    // Shared preferences
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString(MainActivity.ACCESS_TOKEN_KEY, "")
        if (accessToken != "") {
            // Membangung object intent untuk membuka main activity
            val openMainActivity = Intent(this@LoginActivity, MainActivity::class.java)
            // Menjalankan activity menggunakan intent
            startActivity(openMainActivity)
        }
        // Inisialisasi object
        btnLogin = findViewById(R.id.button_login)
        btnRegister = findViewById(R.id.button_register)
        editEmail = findViewById(R.id.edit_email)
        editPassword = findViewById(R.id.edit_password)

        btnLogin.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.button_login -> {
                // Mengambil data dari text entry
                val inputEmail = editEmail.text.toString().trim()
                val inputPassword = editPassword.text.toString().trim()
                var invalidEntries = false

                // Validasi input
                if (inputEmail.isEmpty()) {
                    invalidEntries = true
                    editEmail.setError("Email is empty")
                }
                if (inputPassword.isEmpty()) {
                    invalidEntries = true
                    editPassword.setError("Password is empty")
                }

                if (!invalidEntries) {
                    // Proses login
                    val userLogin = LoginRequest()
                    userLogin.username = inputEmail
                    userLogin.password = inputPassword
                    ApiConfig.instanceRetrofit.login(userLogin)
                        .enqueue(object : retrofit2.Callback<ResponseLogin>{
                            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                                Toast.makeText(this@LoginActivity, t.message, Toast.LENGTH_LONG).show()
                            }

                            override fun onResponse(
                                call: Call<ResponseLogin>,
                                response: Response<ResponseLogin>
                            ) {
                                if (response.isSuccessful) {

                                    val data = response.body()
                                    if (data != null) {
                                        val editor = sharedPreferences.edit()
                                        editor.putString(MainActivity.ACCESS_TOKEN_KEY, data.accessToken)
                                        editor.putString(MainActivity.REFRESH_TOKEN_KEY, data.refreshToken)
                                        editor.commit()
                                        // Membangung object intent untuk membuka main activity
                                        val openMainActivity = Intent(this@LoginActivity, MainActivity::class.java)
                                        // Menjalankan activity menggunakan intent
                                        startActivity(openMainActivity)
                                    }
                                } else {
                                    Toast.makeText(this@LoginActivity, response.errorBody().toString(), Toast.LENGTH_LONG).show()
                                }
                            }
                        })
                }
            }
        }
    }
}