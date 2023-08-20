package com.overdevx.ecommerce.auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.overdevx.ecommerce.R
import com.overdevx.ecommerce.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityRegisterBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)

        binding.btnSignIn.setOnClickListener {
            val intent= Intent(this@RegisterActivity,LoginActivity::class.java)
            startActivity(intent)
        }

        auth=FirebaseAuth.getInstance()
        binding.btnSignUp.setOnClickListener {
            val email=binding.etEmail.text.toString()
            val pass=binding.etPassword.text.toString()
            val uname=binding.etUname.text.toString()

            register(uname,email,pass)
        }
        binding.linearLayout2.setOnClickListener {
            val intent= Intent(this@RegisterActivity,VerificationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun register(name:String,email:String,password:String) {
        if(email.isNotEmpty() && password.isNotEmpty()){
            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this){task ->
                    if(task.isSuccessful){
                        sendEmailVerif(name,password)
                    }else{
                        Toast.makeText(this, "Registrasi gagal: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }else{
            Toast.makeText(this, "Isi semua kolom!", Toast.LENGTH_SHORT).show()

        }
    }

    private fun sendEmailVerif(uname: String,pass: String) {
       val user = auth.currentUser
        user?.sendEmailVerification()?.addOnCompleteListener{ task ->
            if(task.isSuccessful){
                Toast.makeText(
                    this,
                    "Verifikasi email telah dikirim ke ${user.email}",
                    Toast.LENGTH_SHORT
                ).show()
                saveUserDataToFirestore(user,uname,pass)

            }else{
                Toast.makeText(
                    this,
                    "Gagal mengirim verifikasi email: ${task.exception?.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun saveUserDataToFirestore(user: FirebaseUser, uname: String,pass:String) {
        val db = FirebaseFirestore.getInstance()
        val userDocumentRef = db.collection("users").document(user.uid)

        val userData = hashMapOf(
            "email" to user.email,
            "username" to uname,
            "password" to pass
        )

        userDocumentRef.set(userData)
            .addOnSuccessListener {
                Toast.makeText(
                    this,
                    "Berhasil Menyimpan",
                    Toast.LENGTH_SHORT
                ).show()
                val sharedPreferences = getSharedPreferences("userPref", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putBoolean("isLoggedIn", true)
                editor.putString("email",user.email)
                editor.putString("username",uname)
                editor.putString("password",pass)
                editor.apply()
                val intent=Intent(this@RegisterActivity,VerificationActivity::class.java)
                startActivity(intent)
            }
            .addOnFailureListener { e ->
                Toast.makeText(
                    this,
                    "Gagal menyimpan data pengguna: ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}