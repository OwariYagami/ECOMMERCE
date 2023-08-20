package com.overdevx.ecommerce.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.overdevx.ecommerce.R
import com.overdevx.ecommerce.adapter.SliderAdapter
import com.overdevx.ecommerce.databinding.FragmentProductListBinding
import com.overdevx.ecommerce.databinding.FragmentProductallBinding
import com.overdevx.ecommerce.viewmodel.ProductViewModel
import com.smarteist.autoimageslider.SliderView

class ProductallFragment : Fragment() {

    private var _binding: FragmentProductallBinding? = null
    private val binding get() = _binding!!
    private lateinit var productAdapter: ProductAdapter
    private lateinit var newProductAdapter: NewProductAdapter
    private lateinit var productViewModel: ProductViewModel
    lateinit var imageUrl: ArrayList<String>
    lateinit var sliderAdapter: SliderAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentProductallBinding.inflate(inflater, container, false)
        val root: View = binding.root
        productViewModel = ViewModelProvider(requireActivity()).get(ProductViewModel::class.java)

        // Ambil data produk dari ViewModel
        productViewModel.products.observe(viewLifecycleOwner, Observer { products ->
            // Initialize RecyclerView, Adapter, and setLayoutManager
            productAdapter = ProductAdapter(products)
            binding.recyclerProdukall.apply {
                layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
                adapter = productAdapter
            }
        })
        productViewModel.products.observe(viewLifecycleOwner, Observer { products ->
            // Initialize RecyclerView, Adapter, and setLayoutManager
            newProductAdapter = NewProductAdapter(products)
            binding.recyclerProduknew.apply {
                layoutManager = LinearLayoutManager(requireContext())
                adapter = newProductAdapter
            }
        })

        // Ambil data produk dari Firestore
        productViewModel.fetchProductsFromFirestore()


        imageUrl = ArrayList()
        imageUrl =
            (imageUrl + "https://firebasestorage.googleapis.com/v0/b/ecommerce-79c2f.appspot.com/o/Autumn%20Loose%20Jacket%20Men%20-%20Black%20_%20140cm.jpg?alt=media&token=323e6f04-8ac4-4a98-8e41-7ef44d42aabc") as ArrayList<String>
        imageUrl =
            (imageUrl + "https://firebasestorage.googleapis.com/v0/b/ecommerce-79c2f.appspot.com/o/Cute%20Best%20Tee%20Shirts%20For%20Women%2C%20Nice%20Graphic%20Women's%20Tees.jpg?alt=media&token=b43e35f9-cfe2-4c22-9a7f-3767a9916ec9") as ArrayList<String>
        imageUrl =
            (imageUrl + "https://firebasestorage.googleapis.com/v0/b/ecommerce-79c2f.appspot.com/o/Fashion%20Men%20Casual%20Work%20Cotton%20Blend%20Elastic%20Waist%20Long%20Pants%20Trousers%20Simple%20L%20Big%20And%20Tall%20Pants%20Khaki-XXL.jpg?alt=media&token=66d646c7-3ca7-4a36-92c5-ec2ca6adcc50") as ArrayList<String>
        sliderAdapter = SliderAdapter( imageUrl)
        // for our slider view from left to right.
        binding.slider.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR

        // on below line we are setting adapter for our slider.
        binding.slider.setSliderAdapter(sliderAdapter)

        // on below line we are setting scroll time
        // in seconds for our slider view.
        binding.slider.scrollTimeInSec = 3

        // on below line we are setting auto cycle
        // to true to auto slide our items.
        binding.slider.isAutoCycle = true

        // on below line we are calling start
        // auto cycle to start our cycle.
        binding.slider.startAutoCycle()
//        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
//        binding.scrollView.setOnTouchListener{_, event ->
//            if(event.action==MotionEvent.ACTION_DOWN) {
//                viewPager?.requestDisallowInterceptTouchEvent(true)
//            }else if(event.action==MotionEvent.ACTION_UP || event.action==MotionEvent.ACTION_CANCEL){
//                viewPager?.requestDisallowInterceptTouchEvent(false)
//            }
//            false
//        }
        return root
    }
}