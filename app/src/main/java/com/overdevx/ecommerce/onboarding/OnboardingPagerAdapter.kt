package com.overdevx.ecommerce.onboarding

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.overdevx.ecommerce.R
class OnboardingPagerAdapter(private val context: Context, private val onboardingItems:List<OnboardingItem>) :PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val inflater = LayoutInflater.from(context)
        val layout = inflater.inflate(R.layout.onboarding_item, container, false)

        layout.findViewById<ImageView>(R.id.iv_img).setImageResource(onboardingItems[position].img)
        layout.findViewById<TextView>(R.id.tv_title).text = onboardingItems[position].title
        layout.findViewById<TextView>(R.id.tv_desc).text = onboardingItems[position].desc

        container.addView(layout)
        return layout
    }

    override fun getCount(): Int {
       return onboardingItems.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}