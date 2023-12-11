package com.ca214.kemah

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.ca214.kemah.data.api.ApiConfig
import com.ca214.kemah.data.models.requests.RegisterRequest
import com.ca214.kemah.data.models.responses.RegisterResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    // Deklarasi object yang terhubung dengan layout
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button
    private lateinit var editEmail: EditText
    private lateinit var editUsername: EditText
    private lateinit var editPassword: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Inisialisasi activity dengan layout
        btnLogin = findViewById(R.id.button_login)
        btnRegister = findViewById(R.id.button_register)
        editEmail = findViewById(R.id.edit_email)
        editUsername = findViewById(R.id.edit_username)
        editPassword = findViewById(R.id.edit_password)

        // Activasi event click pada tombol register dan login
        btnRegister.setOnClickListener(this)
        btnLogin.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when(view?.id) {
            R.id.button_login -> {
                finish()
            }
            R.id.button_register -> {
                // Mengambil data dari text entry
                val inputEmail = editEmail.text.toString().trim()
                val inputUsername = editUsername.text.toString().trim()
                val inputPassword = editPassword.text.toString().trim()
                var invalidEntries = false

                // Validasi input
                if (inputEmail.isEmpty()) {
                    invalidEntries = true
                    editEmail.setError("Email is empty")
                }
                if (inputUsername.isEmpty()) {
                    invalidEntries = true
                    editUsername.setError("Username is empty")
                }
                if (inputPassword.isEmpty()) {
                    invalidEntries = true
                    editPassword.setError("Password is empty")
                }

                if (!invalidEntries) {
                    // Membuat register request object
                    val userData = RegisterRequest(
                        email = inputEmail,
                        username = inputUsername,
                        password = inputPassword
                    )

                    // Melakukan API call untuk register
                    ApiConfig.instanceRetrofit.register(userData)
                        .enqueue(object : Callback<RegisterResponse> {
                            override fun onResponse(
                                call: Call<RegisterResponse>,
                                response: Response<RegisterResponse>
                            ) {
                                if (response.isSuccessful) {
                                    val userDataResponse = response.body()
                                    Toast.makeText(this@RegisterActivity, "User ${userDataResponse?.username} registration success", Toast.LENGTH_LONG).show()
                                    finish()
                                } else {
                                    Toast.makeText(this@RegisterActivity, "User registration failed", Toast.LENGTH_LONG).show()
                                }
                            }

                            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                                Toast.makeText(this@RegisterActivity, t.localizedMessage, Toast.LENGTH_LONG).show()
                            }
                        })
                }
            }
        }
    }
}