//package com.ca214.kemah
//
//import android.app.Application
//import android.content.Intent
//import com.ca214.kemah.utils.TokenManager
//import javax.inject.Inject
//
//class MyApplication : Application() {
//    @Inject
//    lateinit var tokenManager: TokenManager
//
//    override fun onCreate() {
//        super.onCreate()
//        tokenManager = TokenManager(context = this@MyApplication.applicationContext)
//        val accessToken = tokenManager.getAccessToken()
//        if (accessToken != null) {
//            startActivity(Intent(this, MainActivity::class.java))
//        } else {
//            startActivity(Intent(this, MainActivity::class.java))
//        }
//    }
//}