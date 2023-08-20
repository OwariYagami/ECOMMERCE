package com.overdevx.ecommerce.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.overdevx.ecommerce.model.Cart
import com.overdevx.ecommerce.model.Color
import com.overdevx.ecommerce.model.Product

class ProductViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val productsData = MutableLiveData<List<Product>>()
    private val categoriesData = MutableLiveData<List<String>>()
    private val _product = MutableLiveData<Product?>()
    val product: MutableLiveData<Product?> = _product
    val products: LiveData<List<Product>> = productsData
    // LiveData untuk menyimpan daftar keranjang
    private val _cartItems = MutableLiveData<List<Cart>>()
    val cartItems: LiveData<List<Cart>> = _cartItems
    // Fungsi untuk mengambil daftar produk dari Firestore
    fun getProductsData(): LiveData<List<Product>> {
        db.collection("products")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val productsList = mutableListOf<Product>()
                for (document in querySnapshot) {
                    val product = document.toObject(Product::class.java)
                    productsList.add(product)
                }
                productsData.value = productsList
            }
            .addOnFailureListener { exception ->
                // Gagal mengambil data produk
                productsData.value = emptyList()
            }
        return productsData
    }
    fun getProductById(idProduk: String, onSuccess: (Product?) -> Unit, onFailure: () -> Unit) {
        db.collection("products")
            .whereEqualTo("id_produk", idProduk)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val product = querySnapshot.documents[0].toObject(Product::class.java)
                    onSuccess(product)
                } else {
                    onFailure()
                }
            }
            .addOnFailureListener { exception ->
                onFailure()
            }
    }
    fun generateCartId(onSuccess: (String) -> Unit, onFailure: () -> Unit) {
        db.collection("cart")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val nextId = "CR" + (querySnapshot.size() + 1).toString().padStart(3, '0')
                onSuccess(nextId)
            }
            .addOnFailureListener { exception ->
                onFailure()
            }
    }

    // Fungsi untuk menambah produk ke Firestore
    fun addProduct(product: Product, callback: (Boolean) -> Unit) {
        db.collection("products")
            .add(product)
            .addOnSuccessListener { documentReference ->
                val productId = documentReference.id
                callback(true)
            }
            .addOnFailureListener { exception ->
                // Gagal menambah produk
                callback(false)
            }
    }

    // Fungsi untuk menambah warna produk ke Firestore
    fun addProductColor(productColor: Color, callback: (Boolean) -> Unit) {
        db.collection("colors")
            .add(productColor)
            .addOnSuccessListener { documentReference ->
                val colorId = documentReference.id
                callback(true)
            }
            .addOnFailureListener { exception ->
                // Gagal menambah warna produk
                callback(false)
            }
    }

    // Fungsi untuk mengambil daftar kategori produk dari Firestore
    fun getProductCategories(): LiveData<List<String>> {
        val categoriesData = MutableLiveData<List<String>>()

        db.collection("products")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val categoriesList = querySnapshot.documents.map { it.getString("kategori") ?: "" }
                categoriesData.value = categoriesList.distinct()
            }
            .addOnFailureListener { exception ->
                // Gagal mengambil data kategori produk
                categoriesData.value = emptyList()
            }

        return categoriesData
    }

    fun getProductsByCategory(category: String): LiveData<List<Product>> {
        val productsData = MutableLiveData<List<Product>>()
        db.collection("products")
            .whereEqualTo("kategori", category)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val productsList = mutableListOf<Product>()
                for (document in querySnapshot) {
                    val product = document.toObject(Product::class.java)
                    productsList.add(product)
                }
                productsData.value = productsList
            }
            .addOnFailureListener { exception ->
                // Gagal mengambil data produk
                productsData.value = emptyList()
            }
        return productsData
    }

    fun fetchProductsFromFirestore() {
        db.collection("products")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val productList = mutableListOf<Product>()
                for (document in querySnapshot.documents) {
                    val product = document.toObject(Product::class.java)
                    product?.let { productList.add(it) }
                }
                productsData.value = productList
            }
            .addOnFailureListener { exception ->
                // Gagal mengambil data produk
            }
    }

    fun fetchcolor(productId: String): LiveData<List<Color>> {
        val colorLiveData = MutableLiveData<List<Color>>()

        db.collection("colors")
            .whereEqualTo("id_produk", productId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val colorList = mutableListOf<Color>()
                for (document in querySnapshot.documents) {
                    val color = document.toObject(Color::class.java)
                    if (color != null) {
                        colorList.add(color)
                    }
                }
                colorLiveData.value = colorList
            }
            .addOnFailureListener { exception ->
                // Handle error
            }

        return colorLiveData
    }


    fun getSizesByColorAndProductId(color: String, productId: String): LiveData<List<String>> {
        val sizesLiveData = MutableLiveData<List<String>>()

        val db = FirebaseFirestore.getInstance()
        val colorsRef = db.collection("colors")

        colorsRef
            .whereEqualTo("id_produk", productId)
            .whereEqualTo("warna", color)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val sizes = mutableListOf<String>()

                for (document in querySnapshot.documents) {
                    val colorData = document.toObject(Color::class.java)
                    colorData?.ukuran?.let { sizes.addAll(it) }
                }

                sizesLiveData.postValue(sizes)
            }
            .addOnFailureListener { exception ->
                // Handle error
            }

        return sizesLiveData
    }

    fun getStockByColorSizeAndProductId(color: String, size: String, productId: String): LiveData<Int> {
        val stockLiveData = MutableLiveData<Int>()

        val db = FirebaseFirestore.getInstance()
        val colorsRef = db.collection("colors")

        colorsRef
            .whereEqualTo("id_produk", productId)
            .whereEqualTo("warna", color)
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    val colorData = document.toObject(Color::class.java)
                    val indexOfSize = colorData?.ukuran?.indexOf(size)
                    if (indexOfSize != null && indexOfSize >= 0 && indexOfSize < colorData.stok.size) {
                        stockLiveData.postValue(colorData.stok[indexOfSize])
                    } else {
                        stockLiveData.postValue(0) // Ukuran tidak ditemukan atau stok tidak tersedia
                    }
                }
            }
            .addOnFailureListener { exception ->
                // Handle error
            }

        return stockLiveData
    }

    fun fetchCartItemsByUserId(userId: String) {
        db.collection("cart")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val cartList = mutableListOf<Cart>()
                for (document in querySnapshot) {
                    val cartItem = document.toObject(Cart::class.java)
                    cartList.add(cartItem)
                }
                _cartItems.value = cartList
            }
            .addOnFailureListener { exception ->
                // Gagal mengambil daftar keranjang
            }
    }

    fun getCartbyId(userId: String): LiveData<List<Cart>> {
        val cartdata = MutableLiveData<List<Cart>>()
        db.collection("cart")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val cartlist = mutableListOf<Cart>()
                for (document in querySnapshot) {
                    val cart = document.toObject(Cart::class.java)
                    cartlist.add(cart)
                }
                cartdata.value = cartlist
            }
            .addOnFailureListener { exception ->
                // Gagal mengambil data produk
                cartdata.value = emptyList()
            }
        return cartdata
    }

    fun deleteSelectedCarts(selectedCarts: List<Cart>) {
        val db = FirebaseFirestore.getInstance()

        for (cart in selectedCarts) {
            val cartRef = db.collection("cart").whereEqualTo("id", cart.id)
            cartRef.get().addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    document.reference.delete()
                        .addOnSuccessListener {

                            Log.d(TAG, "Cart deleted successfully")
                        }
                        .addOnFailureListener { e ->
                            Log.e(TAG, "Error deleting cart", e)
                        }
                }
            }
        }
    }

    // Fungsi untuk memperbarui LiveData yang diamati oleh aktivitas atau fragmen
    private fun refreshCartData() {
        // Ambil data terbaru dari sumber data (contoh: Firestore) dan perbarui LiveData carts
        // Contoh:
        val db = FirebaseFirestore.getInstance()
        db.collection("cart")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val updatedCarts = mutableListOf<Cart>()
                for (document in querySnapshot.documents) {
                    val cart = document.toObject(Cart::class.java)
                    cart?.let {
                        updatedCarts.add(cart)
                    }
                }
                _cartItems.value = updatedCarts
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error fetching carts", exception)
            }
    }




}