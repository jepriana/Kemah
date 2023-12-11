package com.ca214.kemah

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.ca214.kemah.data.api.ApiConfig
import com.ca214.kemah.data.models.requests.LoginRequest
import com.ca214.kemah.data.models.responses.LoginResponse
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    // Deklarasi object yang terhubung dengan layout
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button
    private lateinit var editEmail: EditText
    private lateinit var editPassword: EditText

    // Deklarasi object shared preferences
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Membentuk object shared preferences
        sharedPreferences = getSharedPreferences(MainActivity.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        val accessToken = sharedPreferences.getString(MainActivity.ACCESS_TOKEN, null)
        if (accessToken != null) {
            navigateToMainActivity()
        }
        // Inisialisasi object
        btnLogin = findViewById(R.id.button_login)
        btnRegister = findViewById(R.id.button_register)
        editEmail = findViewById(R.id.edit_email)
        editPassword = findViewById(R.id.edit_password)

        btnLogin.setOnClickListener(this)
        btnRegister.setOnClickListener(this)
    }

    fun navigateToMainActivity() {
        // Membangung object intent untuk membuka main activity
        val openMainActivity = Intent(this@LoginActivity, MainActivity::class.java)
        // Menjalankan activity menggunakan intent
        startActivity(openMainActivity)
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.button_register -> {
                val navigatorToRegister = Intent(this@LoginActivity, RegisterActivity::class.java)
                startActivity(navigatorToRegister)
            }
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
                    // Melakukan proses login sederhana
                    val userLogin = LoginRequest()
                    userLogin.username = inputEmail
                    userLogin.password = inputPassword
                    // Melakukan API call menggunakan Retrofit
                    ApiConfig.instanceRetrofit.login(userLogin)
                        .enqueue(object : Callback<LoginResponse> {
                            override fun onResponse(
                                call: Call<LoginResponse>,
                                response: Response<LoginResponse>
                            ) {
                                if (response.isSuccessful) {
                                    val data = response.body()
                                    if (data != null) {
                                        val editor = sharedPreferences.edit()
                                        editor.putString(MainActivity.ACCESS_TOKEN, data.accessToken)
                                        editor.putString(MainActivity.REFRESH_TOKEN, data.refreshToken)
                                        editor.commit()

                                        Toast.makeText(this@LoginActivity, "Login Success", Toast.LENGTH_LONG).show()
                                        navigateToMainActivity()
                                    }
                                } else {
                                    Toast.makeText(this@LoginActivity, "Login Failed: Please try again!", Toast.LENGTH_LONG).show()
                                }
                            }

                            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                                Toast.makeText(this@LoginActivity, t.localizedMessage, Toast.LENGTH_LONG).show()
                            }

                        })
                }
            }
        }
    }
}