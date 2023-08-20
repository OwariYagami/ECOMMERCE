package com.overdevx.ecommerce.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.overdevx.ecommerce.R
import com.overdevx.ecommerce.model.CheckboxItem
import com.overdevx.ecommerce.model.Color

class CheckboxAdapter(private val colors: List<Color>) :
    RecyclerView.Adapter<CheckboxAdapter.ViewHolder>() {
    private var selectedItemPosition = RecyclerView.NO_POSITION
    private var itemClickListener: ((Color) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_checkbox, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position:Int) {
        val color = colors[position]

        holder.colorNameTextView.text = color.warna
        holder.itemView.isSelected = position == selectedItemPosition
//        // Menampilkan atau menyembunyikan gambar checklist
//        if (position == selectedItemPosition) {
//            holder.checkImage.visibility = View.VISIBLE
//        } else {
//            holder.checkImage.visibility = View.GONE
//        }

        holder.itemView.setOnClickListener {
            selectedItemPosition = position
            notifyDataSetChanged()
            itemClickListener?.invoke(color)

        }
    }

    override fun getItemCount(): Int = colors.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val colorNameTextView: TextView = itemView.findViewById(R.id.tv_colorname)


    }
    fun setOnItemClickListener(listener: (Color) -> Unit) {
        itemClickListener = listener
    }

}