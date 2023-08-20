package com.overdevx.ecommerce.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import com.overdevx.ecommerce.R
import com.overdevx.ecommerce.adapter.ColorAdapter
import com.overdevx.ecommerce.adapter.ProductImageAdapter
import com.overdevx.ecommerce.databinding.ActivityDetailBinding
import com.overdevx.ecommerce.viewmodel.ProductViewModel

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var productViewModel: ProductViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDetailBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)

        productViewModel=ViewModelProvider(this@DetailActivity).get(ProductViewModel::class.java)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val img=intent.getStringArrayExtra("imgproduk")
        val imglist=img?.toList() ?: emptyList()
        val namaprd=intent.getStringExtra("nama_produk")
        val idprd=intent.getStringExtra("id_produk")
        val hargaprd=intent.getStringExtra("harga_produk")
        val descprd=intent.getStringExtra("desc_produk")

        binding.tvNamaprd.text=namaprd
        binding.tvHargaprd.text=hargaprd
        binding.tvDetailprd.text=descprd

        val adapter =ProductImageAdapter(imglist)
        binding.viewPager.adapter=adapter

        TabLayoutMediator(binding.tabLayout,binding.viewPager){tab,position ->

        }.attach()
        val colorAdapter =ColorAdapter(emptyList())
        binding.recyclerColor.adapter=colorAdapter
        binding.recyclerColor.layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)

        productViewModel.fetchcolor(idprd!!).observe(this,{colors ->
           colorAdapter.updateData(colors)
        })

        binding.btnAtc.setOnClickListener {
            // Buka bottom sheet dengan membawa id_produk
            val bottomSheetFragment = AddToCartBottomSheetFragment()
            val bundle = Bundle()
            bundle.putString("productId", idprd) // Ganti dengan id_produk yang sesuai
            bottomSheetFragment.arguments = bundle
            bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_search -> {
                Toast.makeText(this@DetailActivity, "Search", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.menu_share -> {
                Toast.makeText(this@DetailActivity, "Share", Toast.LENGTH_SHORT).show()
                return true
            }
            // Tambahkan kasus lain sesuai kebutuhan
            else -> return super.onOptionsItemSelected(item)
        }
    }
}