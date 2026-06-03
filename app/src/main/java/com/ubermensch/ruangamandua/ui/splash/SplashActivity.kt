package com.ruangaman.app.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import kotlin.jvm.java
import androidx.appcompat.app.AppCompatActivity
import com.ubermensch.ruangamandua.databinding.ActivitySplashBinding
import com.ruangaman.app.ui.auth.AuthActivity
import com.ruangaman.app.ui.dashboard.MainActivity
import com.ruangaman.app.utils.SessionManager

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sessionManager = SessionManager(this)

        Handler(Looper.getMainLooper()).postDelayed({
            val dest = if (sessionManager.isLoggedIn()) MainActivity::class.java
            else AuthActivity::class.java
            startActivity(Intent(this, dest))
            finish()
        }, 2000)
    }
}