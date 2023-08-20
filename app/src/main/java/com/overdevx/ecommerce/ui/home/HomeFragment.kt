package com.overdevx.ecommerce.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.map
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.FirebaseFirestore
import com.overdevx.ecommerce.adapter.ProductPagerAdapter
import com.overdevx.ecommerce.auth.LoginActivity
import com.overdevx.ecommerce.databinding.FragmentHomeBinding
import com.overdevx.ecommerce.model.Product
import com.overdevx.ecommerce.viewmodel.ProductViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val db=FirebaseFirestore.getInstance()
    private val products = mutableListOf<Product>()
    private lateinit var productAdapter: ProductAdapter
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)

        val sharedPreferences = context?.getSharedPreferences("USERPREF", Context.MODE_PRIVATE)
        val username=sharedPreferences?.getString("userName","")
        binding.tvUsername.text="Hi, $username"
        binding.btnCart.setOnClickListener {
            val intent=Intent(requireContext(),CartActivity::class.java)
            startActivity(intent)
        }
        productAdapter = ProductAdapter(products)
        // Ambil daftar kategori produk dari ViewModel
        productViewModel.getProductCategories().observe(viewLifecycleOwner, Observer { categories ->
            val allCategory = "All"
            // Pastikan "All" hanya ditambahkan sekali
            val updatedCategories = if (!categories.contains(allCategory)) {
                listOf(allCategory) + categories
            } else {
                categories
            }

            val pagerAdapter = ProductPagerAdapter(requireActivity(), updatedCategories)
            binding.viewPager.adapter = pagerAdapter
            binding.viewPager.isUserInputEnabled=false
            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                tab.text = updatedCategories[position]
            }.attach()
            // Pindahkan tab "All" ke posisi pertama
            binding.tabLayout.getTabAt(updatedCategories.indexOf(allCategory))?.select()

        })

        binding.tvUsername.setOnClickListener {
            val sharedPreferences = requireContext().getSharedPreferences("USERPREF", Context.MODE_PRIVATE)
            val editor = sharedPreferences.edit()
            editor.putBoolean("isLoggedIn",false)
            editor.putString("userId", "")
            editor.putString("userName", "")
            editor.putString("userEmail", "")
            editor.putString("userPassword", "")
            editor.apply()

            val intent=Intent(requireContext(),LoginActivity::class.java)
            startActivity(intent)
        }
        return root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}