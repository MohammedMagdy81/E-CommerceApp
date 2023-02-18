package com.example.e_commerce.fragments.shopping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_commerce.R
import com.example.e_commerce.activities.ShoppingActivity
import com.example.e_commerce.adapters.ColorsAdapter
import com.example.e_commerce.adapters.SizesAdapter
import com.example.e_commerce.adapters.ViewPagerProductDetailAdapter
import com.example.e_commerce.data.Product
import com.example.e_commerce.databinding.FragmentProductDetailBinding
import com.example.e_commerce.utils.hideBottomNav
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProductDetailFragment : Fragment() {

    private lateinit var binding: FragmentProductDetailBinding
    private val viewPagerAdapter by lazy { ViewPagerProductDetailAdapter() }
    private val colorsAdapter by lazy { ColorsAdapter() }
    private val sizesAdapter by lazy { SizesAdapter() }

    private val args by navArgs<ProductDetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hideBottomNav()
        binding = FragmentProductDetailBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val product = args.product
        setupViewPagerAdapter()
        setupColorsAdapter()
        setupSizesAdapter()
        setupData(product)

        binding.productDetailCloseIcon.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupData(product: Product) {
        binding.apply {
            productDetailProductName.text = product.name
            productDetailProductPrice.text = "$ ${product.price}"
            productDetailProductDesc.text = product.desc
            if (product.colors.isNullOrEmpty())
                productDetailTvColors.visibility = View.GONE
            if (product.sizes.isNullOrEmpty())
                productDetailTvSizes.visibility = View.GONE

        }
        viewPagerAdapter.differList.submitList(product.images)
        product.colors?.let {
            colorsAdapter.diffList.submitList(it)
        }
        product.sizes?.let {
            sizesAdapter.diffList.submitList(it)
        }

    }

    private fun setupSizesAdapter() {
        binding.apply {
            productDetailSizesRv.adapter = sizesAdapter
            productDetailSizesRv.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupColorsAdapter() {
        binding.apply {
            productDetailColorsRv.adapter = colorsAdapter
            productDetailColorsRv.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setupViewPagerAdapter() {
        binding.apply {
            productDetailPager.adapter = viewPagerAdapter
        }
    }

}














