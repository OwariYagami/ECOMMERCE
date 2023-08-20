package com.overdevx.ecommerce.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.overdevx.ecommerce.R
import com.overdevx.ecommerce.model.Product

class NewProductAdapter (private val products:List<Product>) : RecyclerView.Adapter<NewProductAdapter.ProductViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ProductViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_newproduct, parent, false)
        return ProductViewHolder(view)
    }
    inner class ProductViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var name: TextView = itemView.findViewById(R.id.tv_namaproduk)
        var desc: TextView = itemView.findViewById(R.id.tv_descproduk)
        var image: ImageView = itemView.findViewById(R.id.iv_imgproduk)
        var price: TextView = itemView.findViewById(R.id.tv_hargaproduk)

    }
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        Glide.with(holder.itemView.context)
            .load(product.gambar_produk[1])
            .into(holder.image)
        holder.name.text = product.nama_produk
        holder.desc.text = product.deskripsi
        holder.price.text = "Rp ${product.harga}"

    }

    override fun getItemCount(): Int {
        return products.size
    }


}