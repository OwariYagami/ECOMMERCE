package com.overdevx.ecommerce.auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import com.overdevx.ecommerce.MainActivity
import com.overdevx.ecommerce.R
import com.overdevx.ecommerce.databinding.ActivitySplashscreenBinding
import com.overdevx.ecommerce.onboarding.OnboardingActivity

class SplashscreenActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashscreenBinding
    private val splashDuration: Long = 3000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivitySplashscreenBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)

        // Animasi untuk splash logo
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out)
        binding.ivLogo.startAnimation(fadeIn)

        // Handler untuk memulai MainActivity setelah splashDuration
        Handler().postDelayed({
            // Cek apakah user pertama kali menginstall aplikasi atau tidak
            val sharedPreferences = getSharedPreferences("USERPREF", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
            val isFirstInstall = sharedPreferences.getBoolean("isFirstInstall", true)

            if (isFirstInstall) {
                // Jika user pertama kali install, arahkan ke OnboardingActivity
                editor.putBoolean("isFirstInstall",false)
                editor.apply()
                startActivity(Intent(this, OnboardingActivity::class.java))
            } else {
                if (isLoggedIn) {
                    // Jika sudah login, arahkan ke MainActivity
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    // Jika belum login, arahkan ke LoginActivity atau activity lain sesuai kebutuhan
                    startActivity(Intent(this, LoginActivity::class.java))
                }
            }
            finish()
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }, splashDuration)
    }

}