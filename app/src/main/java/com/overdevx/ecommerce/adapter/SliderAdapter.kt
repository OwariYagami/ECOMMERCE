package com.overdevx.ecommerce.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.overdevx.ecommerce.R
import com.smarteist.autoimageslider.SliderViewAdapter

class SliderAdapter(imageUrl:ArrayList<String>) : SliderViewAdapter<SliderAdapter.SliderViewHolder>() {
    var sliderList: ArrayList<String> = imageUrl
    override fun getCount(): Int {
        return sliderList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?): SliderAdapter.SliderViewHolder {
        val inflate: View =LayoutInflater.from(parent!!.context).inflate(R.layout.banner_item,null)

        return SliderViewHolder(inflate)
    }

    override fun onBindViewHolder(viewHolder: SliderAdapter.SliderViewHolder?, position: Int) {
       if(viewHolder !=null){
           Glide.with(viewHolder.itemView)
               .load(sliderList.get(position))
               .into(viewHolder.imageView)
       }
    }
    class SliderViewHolder(itemView: View?) : SliderViewAdapter.ViewHolder(itemView) {

        // on below line we are creating a variable for our
        // image view and initializing it with image id.
        var imageView: ImageView = itemView!!.findViewById(R.id.iv_banner)
    }
}