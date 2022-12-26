package com.example.e_commerce.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_commerce.R
import com.example.e_commerce.activities.ShoppingActivity
import com.example.e_commerce.adapters.ColorsAdapter
import com.example.e_commerce.adapters.SizesAdapter
import com.example.e_commerce.adapters.ViewPagerImagesAdapter
import com.example.e_commerce.databinding.FragmentProductDetailsBinding
import com.example.e_commerce.utils.hideBottomNavigation
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProductDetailsFragment : Fragment() {
    private val args by navArgs<ProductDetailsFragmentArgs>()
    private lateinit var binding: FragmentProductDetailsBinding
    private val viewPagerAdapter by lazy { ViewPagerImagesAdapter() }
    private val sizesAdapter by lazy { SizesAdapter() }
    private val colorsAdapter by lazy { ColorsAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hideBottomNavigation()
        binding = FragmentProductDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val product = args.product
        setUpSizesRv()
        setUpColorsRv()
        setUpViewPager()
        setUpClick()
        binding.apply {
            productDetailProductName.text = product.name
            productDetailProductDesc.text = product.description ?: ""
            productDetailProductPrice.text = "$ ${product.price}"
            if (product.sizes.isNullOrEmpty()) {
                productDetailSizeTv.visibility = View.GONE
            }
            if (product.colors.isNullOrEmpty()) {
                productDetailColorTv.visibility = View.GONE
            }
        }
        viewPagerAdapter.differList.submitList(product.images)
        product.colors?.let { colorsAdapter.differList.submitList(it) }
        product.sizes?.let { sizesAdapter.differList.submitList(it) }
    }

    private fun setUpClick() {
        binding.productDetailsCloseIcon.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setUpViewPager() {
        binding.apply {
            productDetailPagerImages.adapter = viewPagerAdapter
        }
    }

    private fun setUpColorsRv() {
        binding.rvColors.apply {
            adapter = colorsAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun setUpSizesRv() {
        binding.rvSizes.apply {
            adapter = sizesAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
    }

}