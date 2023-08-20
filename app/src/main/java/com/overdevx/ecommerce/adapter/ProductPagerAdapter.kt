package com.overdevx.ecommerce.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.ListFragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.overdevx.ecommerce.ui.home.HomeFragment
import com.overdevx.ecommerce.ui.home.ProductListFragment
import com.overdevx.ecommerce.ui.home.ProductallFragment

class ProductPagerAdapter(fragmentActivity: FragmentActivity,private val categories: List<String>) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = categories.size

    override fun createFragment(position: Int): Fragment {
        val category = categories[position]
        return if (category == "All") {
            ProductallFragment() // Menggunakan constructor biasa untuk fragment "All"
        } else {
            ProductListFragment.newInstance(category)
        }
    }
}