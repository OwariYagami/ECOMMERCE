package com.overdevx.ecommerce.ui.home

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import cn.pedant.SweetAlert.SweetAlertDialog
import com.overdevx.ecommerce.R
import com.overdevx.ecommerce.adapter.CartlistAdapter
import com.overdevx.ecommerce.auth.LoadingDialog
import com.overdevx.ecommerce.databinding.ActivityCartBinding
import com.overdevx.ecommerce.model.Cart
import com.overdevx.ecommerce.model.Product
import com.overdevx.ecommerce.viewmodel.ProductViewModel

class CartActivity : AppCompatActivity(),CartlistAdapter.OnItemSelectionChangedListener {
    private lateinit var binding: ActivityCartBinding
    private lateinit var cartlistAdapter: CartlistAdapter
    private lateinit var productViewModel:ProductViewModel
    private val cartlist = mutableListOf<Cart>()
    private lateinit var userid:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCartBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val sharedPreferences = getSharedPreferences("USERPREF", Context.MODE_PRIVATE)
         userid= sharedPreferences.getString("userId","").toString()

        productViewModel= ProductViewModel()
        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)
        productViewModel.cartItems.observe(this, Observer { carts ->
            cartlistAdapter.setData(carts)
        })
        productViewModel.getCartbyId(userid!!).observe(this,{cart->
            // Initialize RecyclerView, Adapter, and setLayoutManager
            cartlistAdapter = CartlistAdapter(cart)
            cartlistAdapter.itemSelectionChangedListener=this
            binding.recyclerCart.layoutManager=LinearLayoutManager(this@CartActivity)
            binding.recyclerCart.adapter = cartlistAdapter
            cartlistAdapter.notifyDataSetChanged()
        })




    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.cart_menu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_hapus -> {
                val selectedCarts = cartlistAdapter.getSelectedItems()
                productViewModel.deleteSelectedCarts(selectedCarts)
                return true
            }
            // Tambahkan kasus lain sesuai kebutuhan
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onItemSelectionChanged(hasSelectedItems: Boolean) {
        val menu = binding.toolbar.menu.findItem(R.id.menu_hapus)
        menu.isVisible = hasSelectedItems
    }

    //  menampilkan loading dialog
    private fun showLoadingDialog() {
        val loadingDialog = LoadingDialog()
        loadingDialog.show(this@CartActivity.supportFragmentManager, "LoadingDialog")
    }

    //  menyembunyikan loading dialog
    private fun hideLoadingDialog() {
        val loadingDialog =
            this@CartActivity.supportFragmentManager.findFragmentByTag("LoadingDialog") as? LoadingDialog
        loadingDialog?.dismiss()
    }

    private fun showSweetAlertSuccess(title:String,content:String){
        val dialog = SweetAlertDialog(this@CartActivity, SweetAlertDialog.SUCCESS_TYPE)
        dialog.setTitleText(title)
        dialog.setContentText(content)
        dialog.setConfirmText("OK")
        dialog.setConfirmClickListener { sDialog ->
            sDialog.dismissWithAnimation()
        }
        dialog.show()
    }
}