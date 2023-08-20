package com.overdevx.ecommerce.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.overdevx.ecommerce.R
import com.overdevx.ecommerce.databinding.FragmentHomeBinding
import com.overdevx.ecommerce.databinding.FragmentProductListBinding
import com.overdevx.ecommerce.model.Product
import com.overdevx.ecommerce.viewmodel.ProductViewModel

class ProductListFragment : Fragment() {

    private var _binding: FragmentProductListBinding? = null
    private val binding get() = _binding!!
    private lateinit var productAdapter: ProductAdapter
    private var category: String = ""
    private lateinit var productViewModel: ProductViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentProductListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         category = arguments?.getString(ARG_CATEGORY) ?: ""

        productViewModel = ViewModelProvider(this).get(ProductViewModel::class.java)

        // Ambil data produk dari ViewModel berdasarkan kategori
        productViewModel.getProductsByCategory(category).observe(viewLifecycleOwner, Observer { products ->
            // Initialize RecyclerView, Adapter, and setLayoutManager
            productAdapter = ProductAdapter(products)
           binding. recyclerProdukall.apply {
                layoutManager = GridLayoutManager(requireContext(),2)
                adapter = productAdapter
            }
        })
    }
    // Buat fungsi newInstance yang menerima list produk
    companion object {
        private const val ARG_CATEGORY = "category"

        fun newInstance(category: String): ProductListFragment {
            val fragment = ProductListFragment()
            val args = Bundle()
            args.putString(ARG_CATEGORY, category)
            fragment.arguments = args
            return fragment
        }
    }
}