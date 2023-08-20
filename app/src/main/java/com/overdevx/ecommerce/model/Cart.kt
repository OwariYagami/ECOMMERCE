package com.overdevx.ecommerce.model

data class Cart(
    val id: String = "",
    val userId: String = "",
    val id_produk: String = "",
    val id_color: String = "",
    val colorName: String = "",
    val size: String = "",
    val quantity: Int = 0,
    val price: Int = 0,
    val productName: String = "",
    val productImage: String = ""
)

