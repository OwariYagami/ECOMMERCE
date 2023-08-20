package com.overdevx.ecommerce.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.overdevx.ecommerce.MainActivity
import com.overdevx.ecommerce.R
import com.overdevx.ecommerce.auth.LoginActivity
import com.overdevx.ecommerce.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOnboardingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityOnboardingBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)

        //Inisialisasi ViewPager
        val onboardingItems = listOf(
            OnboardingItem(R.drawable.img1, "EXPLORE PRODUCTS", "Jelajahi beragam produk dengan kualitas terbaik, dengan beragam kombinasi yang akan membuat hidupmu berwarna"),
            OnboardingItem(R.drawable.img2, "EASY PAYMENT", "Kemudahan dalam sistem pembayaran, menjadi prioritas kami"),
            OnboardingItem(R.drawable.img3, "QUICK DELIVERY", "Kecepatan dalam pengiriman akan memudahkan sistem belanjamu")
        )
        val adapter = OnboardingPagerAdapter(this, onboardingItems)
        binding.viewPager.adapter = adapter

        //Menampilkan Tombol
        binding.viewPager.addOnPageChangeListener(object : androidx.viewpager.widget.ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                if (position == onboardingItems.size - 1) {
                    binding.buttonGetStarted.visibility = View.VISIBLE
                } else {
                    binding.buttonGetStarted.visibility = View.GONE
                }
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        //Pindah ke Main
        binding.buttonGetStarted.setOnClickListener {
           val intent=Intent(this@OnboardingActivity,LoginActivity::class.java)
            startActivity(intent)
        }
    }
}