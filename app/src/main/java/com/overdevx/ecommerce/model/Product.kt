package com.overdevx.ecommerce.model

data class Product(
    val id_produk: String = "",
    val nama_produk: String = "",
    val brand: String = "",
    val harga: Int = 0,
    val deskripsi: String = "",
    val kategori: String = "",
    val gambar_produk: List<String> = emptyList()
)
data class Color(
    val id_color: String = "",
    val id_produk: String = "",
    val warna: String = "",
    val ukuran: List<String> = emptyList(),
    val stok: List<Int> = emptyList(),
    val code: String = ""
){
    fun getMaxQuantity(selectedSize: String?): Int {
        val indexOfSize = ukuran.indexOf(selectedSize)
        if (indexOfSize != -1 && indexOfSize < stok.size) {
            return stok[indexOfSize]
        }
        return 0
    }
}
