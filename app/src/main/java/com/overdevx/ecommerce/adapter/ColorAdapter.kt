package com.overdevx.ecommerce.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.overdevx.ecommerce.R
import com.overdevx.ecommerce.model.Color

class ColorAdapter(private var colorList: List<Color>) :
    RecyclerView.Adapter<ColorAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val item=itemView.findViewById<RelativeLayout>(R.id.rl_color)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_color, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val color = colorList[position]
        holder.item.setBackgroundColor(android.graphics.Color.parseColor(color.code)) // Mengubah warna latar belakang
    }
    // Metode untuk mengganti data dalam adapter
    fun updateData(newColorList: List<Color>) {
        colorList = newColorList
        notifyDataSetChanged()
    }
    override fun getItemCount(): Int = colorList.size
}
