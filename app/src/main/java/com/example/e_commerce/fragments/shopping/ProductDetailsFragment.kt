package com.example.e_commerce.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_commerce.R
import com.example.e_commerce.activities.ShoppingActivity
import com.example.e_commerce.adapters.ColorsAdapter
import com.example.e_commerce.adapters.SizesAdapter
import com.example.e_commerce.adapters.SliderAdapter
import com.example.e_commerce.adapters.ViewPagerImagesAdapter
import com.example.e_commerce.data.CartProduct
import com.example.e_commerce.databinding.FragmentProductDetailsBinding
import com.example.e_commerce.mvvm.ProductDetailViewModel
import com.example.e_commerce.utils.Resources
import com.example.e_commerce.utils.hideBottomNavigation
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.smarteist.autoimageslider.SliderView
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProductDetailsFragment : Fragment() {
    private val args by navArgs<ProductDetailsFragmentArgs>()
    private lateinit var binding: FragmentProductDetailsBinding

    //    private val viewPagerAdapter by lazy { ViewPagerImagesAdapter() }
    private val sliderAdapter by lazy { SliderAdapter(args.product.images) }
    private val sizesAdapter by lazy { SizesAdapter() }
    private val colorsAdapter by lazy { ColorsAdapter() }
    private var selectedColor: Int? = null
    private var selectedSize: String? = null
    private val productDetailViewModel by viewModels<ProductDetailViewModel>()

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
        setUpSlider()
        setUpClick()
        sizesAdapter.onItemClick = {
            selectedSize = it
        }
        colorsAdapter.onItemClick = {
            selectedColor = it
        }

        collectData()
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
        //viewPagerAdapter.differList.submitList(product.images)

        product.colors?.let { colorsAdapter.differList.submitList(it) }
        product.sizes?.let { sizesAdapter.differList.submitList(it) }
    }

    private fun collectData() {
        lifecycleScope.launch {
            productDetailViewModel.addCart.collect {
                when (it) {
                    is Resources.Error -> {
                        //show Toast Error
                        Toasty.error(
                            requireContext(),
                            "some errors happens",
                            Toast.LENGTH_LONG,
                            true
                        ).show()
                    }
                    is Resources.Loading -> {

                    }
                    is Resources.Success -> {
                        Toasty.success(
                            requireContext(),
                            "Product Added Successfully ",
                            Toast.LENGTH_LONG,
                            true
                        ).show()
                        //show Success Toasty
                    }
                }
            }
        }
    }

    private fun setUpSlider() {
        binding.apply {
            slider.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
            slider.setSliderAdapter(sliderAdapter)
            slider.setScrollTimeInSec(3);
            slider.setAutoCycle(true);
            slider.startAutoCycle()
        }

    }

    private fun setUpClick() {
        binding.productDetailsCloseIcon.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.productDetailBtnAddToCart.setOnClickListener {
            productDetailViewModel.addUpdateProductCart(
                CartProduct(
                    args.product,
                    1,
                    selectedColor,
                    selectedSize
                )
            )
        }
    }

//    private fun setUpViewPager() {
//        binding.apply {
//            productDetailPagerImages.adapter = viewPagerAdapter
//        }
//    }

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