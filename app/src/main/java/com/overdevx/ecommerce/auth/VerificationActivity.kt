package com.overdevx.ecommerce.auth

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.firebase.auth.FirebaseAuth

import com.overdevx.ecommerce.R
import com.overdevx.ecommerce.databinding.ActivityVerificationBinding

class VerificationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityVerificationBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var authStateListener: FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVerificationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.btnResend.setOnClickListener {
            showLoadingDialog()
        }
        auth = FirebaseAuth.getInstance()
        val sharedPreferences = getSharedPreferences("userPref", Context.MODE_PRIVATE)
        val pass = sharedPreferences.getString("password", "")
        val email = sharedPreferences.getString("email", "")
        authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            showLoadingDialog()
            val currentUser = firebaseAuth.currentUser
            if (currentUser != null) {
                // Periksa apakah email telah diverifikasi
                currentUser.reload().addOnCompleteListener { reloadTask ->
                    if (reloadTask.isSuccessful) {
                        if (currentUser.isEmailVerified) {
                            // Email telah diverifikasi
                            hideLoadingDialog()
                            showInfo()
                            Toast.makeText(this, "Email telah terverifikasi.", Toast.LENGTH_SHORT)
                                .show()

                        } else {
                            // Email belum diverifikasi, lakukan sign-in dengan email dan password
                            hideLoadingDialog()
                            signInWithEmailAndPassword(email, pass)

                        }
                    } else {
                        // Gagal me-reload pengguna
                        Toast.makeText(
                            this,
                            "Gagal memuat ulang data pengguna: ${reloadTask.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                // Pengguna tidak aktif, redirect ke Activity login atau halaman lain
                // Misalnya:
                // val intent = Intent(this, LoginActivity::class.java)
                // startActivity(intent)
            }
        }


    }

    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(authStateListener)
    }

    override fun onStop() {
        super.onStop()
        auth.removeAuthStateListener(authStateListener)
    }

    private fun signInWithEmailAndPassword(email: String?, password: String?) {
        if (email != null && password != null) {
            showLoadingDialog()
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in berhasil
                        hideLoadingDialog()
                        // Tidak perlu melakukan apa-apa, karena listener authStateListener akan menangani hasil verifikasi email
                    } else {
                        hideLoadingDialog()
                        // Sign in gagal
                        Toast.makeText(
                            this,
                            "Gagal masuk dengan email dan password: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }

    //  menampilkan loading dialog
    private fun showLoadingDialog() {
        val loadingDialog = LoadingDialog()
        loadingDialog.show(supportFragmentManager, "LoadingDialog")
    }

    //  menyembunyikan loading dialog
    private fun hideLoadingDialog() {
        val loadingDialog =
            supportFragmentManager.findFragmentByTag("LoadingDialog") as? LoadingDialog
        loadingDialog?.dismiss()
    }

    private fun showInfo() {
        val dialog = SweetAlertDialog(this@VerificationActivity, SweetAlertDialog.SUCCESS_TYPE)
        dialog.setTitleText("Informasi")
        dialog.setContentText("Email Telah Terverifikasi,Silahkan Login")
        dialog.setConfirmText("OK")
        dialog.setConfirmClickListener { sDialog ->
            sDialog.dismissWithAnimation()
            val intent =
                Intent(this@VerificationActivity, LoginActivity::class.java)
            startActivity(intent)
        }
        dialog.show()
    }

}
