package com.overdevx.ecommerce.ui.home

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.overdevx.ecommerce.R
import com.overdevx.ecommerce.model.Product

class ProductAdapter (private val products:List<Product>) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ProductViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }
    inner class ProductViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var name: TextView = itemView.findViewById(R.id.tv_namaproduk)
        var image: ImageView = itemView.findViewById(R.id.iv_imgproduk)
        var price: TextView = itemView.findViewById(R.id.tv_hargaproduk)
        var item: CardView = itemView.findViewById(R.id.cv_item)

    }
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        Glide.with(holder.itemView.context)
            .load(product.gambar_produk[1])
            .into(holder.image)
        holder.name.text = product.nama_produk
        holder.price.text = "Rp ${product.harga}"
        holder.item.setOnClickListener{
            val intent=Intent(holder.itemView.context,DetailActivity::class.java)
            val imagelist=product.gambar_produk.toTypedArray()
            intent.putExtra("imgproduk",imagelist)
            intent.putExtra("nama_produk",product.nama_produk)
            intent.putExtra("id_produk",product.id_produk)
            intent.putExtra("harga_produk","Rp "+product.harga)
            intent.putExtra("desc_produk",product.deskripsi)

            holder.itemView.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return products.size
    }


}