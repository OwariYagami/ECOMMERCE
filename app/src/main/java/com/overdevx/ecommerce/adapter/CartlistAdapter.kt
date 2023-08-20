package com.overdevx.ecommerce.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.overdevx.ecommerce.R
import com.overdevx.ecommerce.model.Cart
import com.overdevx.ecommerce.model.Color

class CartlistAdapter (private val cartlist:List<Cart>) : RecyclerView.Adapter<CartlistAdapter.CarlistViewHolder>() {
    private val selectedItems = mutableSetOf<Int>()
    private var itemClickListener: ((Cart) -> Unit)? = null
    var itemSelectionChangedListener: OnItemSelectionChangedListener? = null
    private var cartList: List<Cart> = emptyList()
    inner class CarlistViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        var image:ImageView =itemView.findViewById(R.id.iv_imgproduk)
        var namaprd:TextView =itemView.findViewById(R.id.tv_namaprd)
        var colorprd:TextView =itemView.findViewById(R.id.tv_warna)
        var sizeprd:TextView =itemView.findViewById(R.id.tv_size)
        var hargaprd:TextView =itemView.findViewById(R.id.tv_price)
        var qtyprd:TextView =itemView.findViewById(R.id.tv_qty)
        var checkimage:ImageView =itemView.findViewById(R.id.iv_check)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarlistViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.item_cart,parent,false)
        return CarlistViewHolder(view)
    }

    override fun getItemCount(): Int {
       return cartlist.size
    }

    override fun onBindViewHolder(holder: CarlistViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val cart = cartlist[position]
        val isSelected = selectedItems.contains(position)
        holder.itemView.isSelected = isSelected

        holder.itemView.setOnClickListener {
            if (isSelected) {
                selectedItems.remove(position)
            } else {
                selectedItems.add(position)
            }
            notifyDataSetChanged()
            itemClickListener?.invoke(cart)
            itemSelectionChangedListener?.onItemSelectionChanged(!selectedItems.isEmpty())

        }
        // Menampilkan atau menyembunyikan gambar checklist
        if (isSelected) {
            holder.checkimage.visibility = View.VISIBLE
        } else {
            holder.checkimage.visibility = View.GONE
        }
        Glide.with(holder.itemView.context)
            .load(cart.productImage)
            .into(holder.image)

        holder.namaprd.text=cart.productName
        holder.colorprd.text=cart.colorName
        holder.sizeprd.text=cart.size
        holder.hargaprd.text="Rp "+cart.price.toString()
        holder.qtyprd.text=cart.quantity.toString()

    }
    fun getSelectedItems(): List<Cart> {
        return selectedItems.map { position ->
            cartlist[position]
        }
    }
    interface OnItemSelectionChangedListener {
        fun onItemSelectionChanged(hasSelectedItems: Boolean)
    }
    fun setData(newCartList: List<Cart>) {
        cartList = newCartList
        notifyDataSetChanged()
    }


}