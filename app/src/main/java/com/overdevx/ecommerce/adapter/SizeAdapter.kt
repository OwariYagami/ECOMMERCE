package com.overdevx.ecommerce.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.overdevx.ecommerce.R
import com.overdevx.ecommerce.model.Color

class SizeAdapter(private val sizes: List<String>) :
    RecyclerView.Adapter<SizeAdapter.ViewHolder>() {
    private var selectedItemPosition = RecyclerView.NO_POSITION
    private var itemClickListener: ((String) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizeAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_checkbox, parent, false)
        return ViewHolder(view)
    }

    // ViewHolder dan implementasi lainnya sama seperti yang sebelumnya telah dijelaskan

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val size = sizes[position]
        holder.itemView.isSelected = position == selectedItemPosition

        holder.sizeNameTextView.text = size
//        // Menampilkan atau menyembunyikan gambar checklist
//        if (position == selectedItemPosition) {
//            holder.checkImage.visibility = View.VISIBLE
//        } else {
//            holder.checkImage.visibility = View.GONE
//        }
        holder.itemView.setOnClickListener {
            selectedItemPosition = position
            notifyDataSetChanged()
            itemClickListener?.invoke(size)
        }
    }

    override fun getItemCount(): Int = sizes.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val sizeNameTextView: TextView = itemView.findViewById(R.id.tv_colorname)

    }

    fun setOnItemClickListener(listener: (String) -> Unit) {
        itemClickListener = listener
    }
}