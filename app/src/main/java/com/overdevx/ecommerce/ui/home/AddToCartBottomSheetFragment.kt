package com.overdevx.ecommerce.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cn.pedant.SweetAlert.SweetAlertDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.overdevx.ecommerce.MainActivity
import com.overdevx.ecommerce.R
import com.overdevx.ecommerce.adapter.CheckboxAdapter
import com.overdevx.ecommerce.adapter.SizeAdapter
import com.overdevx.ecommerce.auth.LoadingDialog
import com.overdevx.ecommerce.model.Cart
import com.overdevx.ecommerce.model.CheckboxItem
import com.overdevx.ecommerce.model.Color
import com.overdevx.ecommerce.viewmodel.ProductViewModel
import com.overdevx.ecommerce.viewmodel.UserViewModel

class AddToCartBottomSheetFragment:BottomSheetDialogFragment() {
    private lateinit var colorAdapter: CheckboxAdapter
    private lateinit var selectedColor: Color
    private lateinit var selectedSize: String
    private lateinit var sizeAdapter: SizeAdapter
    private lateinit var txtNumber: TextView
    private lateinit var btnPlus: ImageButton
    private lateinit var btnMinus: ImageButton
    private lateinit var btnAtc: Button
    private  var maxQuantity=0
    private lateinit var idcolor:String
    private lateinit var color:String
    private lateinit var prdName:String
    private lateinit var prdImg:String
    private  var price=0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottomsheet_dialog, container, false)

        // Inisialisasi elemen-elemen dalam bottom sheet
        val recyclerViewColors = view.findViewById<RecyclerView>(R.id.recycler_color)
        val recyclerViewSizes = view.findViewById<RecyclerView>(R.id.recycler_size)
        val stok = view.findViewById<TextView>(R.id.tv_stok)
         btnAtc = view.findViewById(R.id.btn_atc)
        txtNumber = view.findViewById(R.id.et_qty)
        btnPlus = view.findViewById(R.id.btn_plus)
        btnMinus = view.findViewById(R.id.btn_minus)
        val sharedPreferences = context?.getSharedPreferences("USERPREF", Context.MODE_PRIVATE)
        val userId=sharedPreferences?.getString("userId","")
        // Mengambil ID produk dari intent atau argument
        val productId = arguments?.getString("productId")

        // Mengambil data warna dan ukuran dari ViewModel
        val productViewModel = ViewModelProvider(requireActivity()).get(ProductViewModel::class.java)
        productViewModel.fetchcolor(productId!!).observe(viewLifecycleOwner, { colors ->
            // Inisialisasi dan set adapter
            colorAdapter = CheckboxAdapter(colors)
            recyclerViewColors.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
            recyclerViewColors.adapter = colorAdapter

            colorAdapter.setOnItemClickListener { selectedColor->
                val selectedColorName = selectedColor.warna
                val selectedProductId = selectedColor.id_produk
                idcolor=selectedColor.id_color
                color=selectedColor.warna

                this.selectedColor =selectedColor
                productViewModel.getSizesByColorAndProductId(selectedColorName, selectedProductId)
                    .observe(this, Observer { sizes ->
                        // Tampilkan daftar ukuran

                        sizeAdapter = SizeAdapter(sizes)
                        recyclerViewSizes.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
                        recyclerViewSizes.adapter = sizeAdapter

                        sizeAdapter.setOnItemClickListener { size->
                            selectedSize= size
                            productViewModel.getStockByColorSizeAndProductId(selectedColorName,size,selectedProductId)
                                .observe(this, Observer { stock->
                                    maxQuantity = stock
                                    stok.text="Stok : $stock"
                                })
                        }
                    })
            }

        })

        btnPlus.setOnClickListener{
            plus()
        }
        btnMinus.setOnClickListener {
            minus()
        }

        productViewModel.getProductById(productId,
            onSuccess = {product ->
                if (product != null) {
                    price=product.harga
                    prdName=product.nama_produk
                    prdImg=product.gambar_produk[0]

                }else{
                    Toast.makeText(requireContext(), "Null", Toast.LENGTH_SHORT).show()
                }

            },
            onFailure = {
                Toast.makeText(requireContext(), "Failure", Toast.LENGTH_SHORT).show()
            })
        btnAtc.setOnClickListener {


            val userViewModel = ViewModelProvider(requireActivity()).get(UserViewModel::class.java)
            productViewModel.generateCartId(
                onSuccess = {idcart ->
                    showLoadingDialog()
                    val cartItem = Cart(
                        id=idcart,
                        userId = userId!!,
                        id_produk = productId,
                        id_color = idcolor,
                        colorName = color,
                        size = selectedSize,
                        quantity = txtNumber.text.toString().toInt(),
                        price = price,
                        productName = prdName,
                        productImage = prdImg
                    )

                    userViewModel.addToCart(cartItem,
                        onSuccess = {
                            hideLoadingDialog()
                          showSweetAlertSuccess("Informasi","Produk Berhasil Ditambah ke Keranjang")
                        },
                        onFailure = {
                            hideLoadingDialog()
                            val dialog = SweetAlertDialog(requireContext(), SweetAlertDialog.ERROR_TYPE)
                            dialog.setTitleText("Informasi")
                            dialog.setContentText("Produk Gagal Ditambah ke Keranjang")
                            dialog.setConfirmText("OK")
                            dialog.setConfirmClickListener { sDialog ->
                                sDialog.dismissWithAnimation()
                            }
                            dialog.show()
                        }
                    )
                },
                onFailure = {

                }
            )

        }
        return view
    }

    private fun minus() {
        var quantity = txtNumber.text.toString().toInt()
        if (quantity > 1) {
            quantity--
            txtNumber.setText(quantity.toString())
        }
    }

    private fun plus() {
        var quantity = txtNumber.text.toString().toInt()
        if (quantity < maxQuantity!!) {
            quantity++
            txtNumber.setText(quantity.toString())
        }
    }

    //  menampilkan loading dialog
    private fun showLoadingDialog() {
        val loadingDialog = LoadingDialog()
        loadingDialog.show(requireActivity().supportFragmentManager, "LoadingDialog")
    }

    //  menyembunyikan loading dialog
    private fun hideLoadingDialog() {
        val loadingDialog =
            requireActivity().supportFragmentManager.findFragmentByTag("LoadingDialog") as? LoadingDialog
        loadingDialog?.dismiss()
    }

    private fun showSweetAlertSuccess(title:String,content:String){
        val dialog = SweetAlertDialog(requireContext(), SweetAlertDialog.SUCCESS_TYPE)
        dialog.setTitleText(title)
        dialog.setContentText(content)
        dialog.setConfirmText("OK")
        dialog.setConfirmClickListener { sDialog ->
            sDialog.dismissWithAnimation()
        }
        dialog.show()
    }

}