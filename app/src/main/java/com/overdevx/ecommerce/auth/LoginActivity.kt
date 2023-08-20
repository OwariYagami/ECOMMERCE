package com.overdevx.ecommerce.auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.overdevx.ecommerce.MainActivity
import com.overdevx.ecommerce.databinding.ActivityLoginBinding
import com.overdevx.ecommerce.model.User
import com.overdevx.ecommerce.viewmodel.UserViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var userViewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        userViewModel=ViewModelProvider(this).get(UserViewModel::class.java)
        auth = FirebaseAuth.getInstance()

        binding.btnSignUp.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
        binding.btnSignin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val pass = binding.etPassword.text.toString()
            signInWithEmailAndPassword(email, pass)
        }
    }

    private fun signInWithEmailAndPassword(email: String?, password: String?) {
        if (email != null && password != null) {
            showLoadingDialog()
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in berhasil
                        val user: FirebaseUser? = auth.currentUser
                        if (user != null) {
                            val userId = user.uid
                            Toast.makeText(this, "id $userId", Toast.LENGTH_SHORT).show()
                            userViewModel.getUserData(userId).observe(this, Observer { user ->
                                saveUserDataToSharedPreferences(this@LoginActivity,user,userId)
                                val sharedPreferences = getSharedPreferences("USERPREF", Context.MODE_PRIVATE)
                                val editor = sharedPreferences.edit()
                                editor.putBoolean("isLoggedIn",true)
                                editor.apply()
                                hideLoadingDialog()
                                showInfo()
                            })
                        }

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
        val dialog = SweetAlertDialog(this@LoginActivity, SweetAlertDialog.SUCCESS_TYPE)
        dialog.setTitleText("Informasi")
        dialog.setContentText("Login Berhasil")
        dialog.setConfirmText("OK")
        dialog.setConfirmClickListener { sDialog ->
            sDialog.dismissWithAnimation()
            val intent =
                Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(intent)
        }
        dialog.show()
    }

    // Fungsi untuk menyimpan data pengguna ke SharedPreferences
    fun saveUserDataToSharedPreferences(context: Context, user: User?,userId:String) {
        val sharedPreferences = context.getSharedPreferences("USERPREF", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        if (user != null) {
            Toast.makeText(this, "Data : ${user.username}", Toast.LENGTH_SHORT).show()
            editor.putString("userId", userId)
            editor.putString("userName", user.username)
            editor.putString("userEmail", user.email)
            editor.putString("userPassword", user.password)
        } else {
            // Jika user null, hapus data dari SharedPreferences
            editor.clear()
        }

        editor.apply()
    }
}