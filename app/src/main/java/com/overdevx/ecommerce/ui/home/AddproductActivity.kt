package com.overdevx.ecommerce.ui.home

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.firebase.firestore.FirebaseFirestore
import com.overdevx.ecommerce.databinding.ActivityAddproductBinding
import com.overdevx.ecommerce.model.Color
import com.overdevx.ecommerce.model.Product
import com.overdevx.ecommerce.viewmodel.ProductViewModel

class AddproductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddproductBinding
    val db = FirebaseFirestore.getInstance()
    private lateinit var productViewModel: ProductViewModel
    private lateinit var selectIdproduk:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddproductBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        // Ambil data produk dari ViewModel
        productViewModel.getProductsData().observe(this, Observer { products ->
            val productNamesList = products.map { it.nama_produk }
            setupSpinner(productNamesList)

            binding.spinnerProducts.onItemSelectedListener=object :AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    selectIdproduk=products[p2].id_produk.toString()
                    Toast.makeText(this@AddproductActivity, "id :$selectIdproduk", Toast.LENGTH_SHORT).show()
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("Not yet implemented")
                }

            }
        })

        binding.btnSaveProduk.setOnClickListener {
            val et_idprd=binding.etIdProduct.text.toString()
            val et_namaprd=binding.etNamaProduct.text.toString()
            val et_brand=binding.etBrand.text.toString()
            val et_harga=binding.etPrice.text.toString()
            val et_desc=binding.etDeskripsi.text.toString()
            val et_kategori=binding.etKategori.text.toString()
            val et_url1=binding.etUrlImage.text.toString()
            val et_url2=binding.etUrlImage2.text.toString()
            val et_url3=binding.etUrlImage3.text.toString()
            val harga:Int=et_harga.toInt()
            // Simpan Produk
            val gambarProduk = listOf(et_url1, et_url2, et_url3)

            val newProduct = Product(
                id_produk = et_idprd,
                nama_produk = et_namaprd,
                brand = et_brand,
                harga = harga,
                deskripsi = et_desc,
                kategori = et_kategori,
                gambar_produk = gambarProduk,

            )
            // Panggil fungsi untuk menambahkan produk
            productViewModel.addProduct(newProduct) { success ->
                if (success) {
                    // Produk berhasil ditambahkan, tampilkan pesan berhasil atau navigasi ke halaman lain
                    showInfo()
                } else {
                    // Produk gagal ditambahkan, tampilkan pesan gagal atau berikan notifikasi kesalahan
                    Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show()
                }
            }


        }
        binding.btnSave.setOnClickListener {
            val et_idwarna=binding.etIdWarna.text.toString()
            val et_warna=binding.etWarna.text.toString()
            val size = listOf("S", "M", "L","XL")
            val stok = listOf(20, 30, 10,25)
            addColor(et_idwarna,selectIdproduk,et_warna,size,stok)
        }
    }


    // Fungsi untuk menambahkan warna ke Firestore
    fun addColor(colorId:String,productId: String, warna: String, ukuran: List<String>, stok: List<Int>)
    {
        val colorData = Color(
            id_color = colorId,
            id_produk = productId,
            warna = warna,
            ukuran = ukuran,
            stok = stok
        )

        productViewModel.addProductColor(colorData){success->
            if(success){
                showInfo()
            }else{
                Toast.makeText(this, "Gagal", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun setupSpinner(productNamesList: List<String>) {
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, productNamesList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerProducts.adapter = adapter
    }
    private fun showInfo() {
        val dialog = SweetAlertDialog(this@AddproductActivity, SweetAlertDialog.SUCCESS_TYPE)
        dialog.setTitleText("Informasi")
        dialog.setContentText("Produk Berhasil Di Simpan")
        dialog.setConfirmText("OK")
        dialog.setConfirmClickListener { sDialog ->
            sDialog.dismissWithAnimation()
        }
        dialog.show()
    }
}