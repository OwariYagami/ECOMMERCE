package com.overdevx.ecommerce.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.overdevx.ecommerce.model.Cart
import com.overdevx.ecommerce.model.User

class UserViewModel:ViewModel() {
    private val db=FirebaseFirestore.getInstance()
    private val userData=MutableLiveData<User?>()

    // Fungsi untuk mengambil data pengguna dari Firebase
    fun getUserData(userId: String): MutableLiveData<User?> {
        db.collection("users")
            .document(userId)
            .get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val user = documentSnapshot.toObject(User::class.java)
                    userData.value = user
                } else {
                    userData.value = null
                }
            }
            .addOnFailureListener { exception ->
                // Gagal mengambil data pengguna
                userData.value = null
            }
        return userData
    }
    fun addToCart(cartItem: Cart, onSuccess: () -> Unit, onFailure: () -> Unit) {
        db.collection("cart")
            .add(cartItem)
            .addOnSuccessListener { documentReference ->
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure()
            }
    }
}