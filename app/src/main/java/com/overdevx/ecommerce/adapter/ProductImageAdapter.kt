package com.overdevx.ecommerce.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.overdevx.ecommerce.R

class ProductImageAdapter(private val images:List<String>):RecyclerView.Adapter<ProductImageAdapter.productViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): productViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product_image, parent, false)
        view.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        return productViewHolder(view)
    }

    class productViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var img_produk=itemView.findViewById<ImageView>(R.id.iv_imgproduk)
    }

    override fun onBindViewHolder(holder:productViewHolder, position: Int) {
        val imageUrl = images[position]
        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .into(holder.img_produk)
    }

    override fun getItemCount(): Int = images.size

}