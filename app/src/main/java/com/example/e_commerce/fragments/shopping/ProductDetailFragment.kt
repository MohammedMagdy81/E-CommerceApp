package com.example.e_commerce.fragments.shopping

import android.graphics.Paint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_commerce.R
import com.example.e_commerce.adapters.ColorsAdapter
import com.example.e_commerce.adapters.SizesAdapter
import com.example.e_commerce.adapters.ViewPagerProductDetailAdapter
import com.example.e_commerce.data.CartProduct
import com.example.e_commerce.data.Product
import com.example.e_commerce.databinding.FragmentProductDetailBinding
import com.example.e_commerce.mvvm.AddToCartViewModel
import com.example.e_commerce.utils.Resources
import com.example.e_commerce.utils.hideBottomNav
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import java.util.*

@AndroidEntryPoint
class ProductDetailFragment : Fragment() {

    private lateinit var binding: FragmentProductDetailBinding
    private val viewPagerAdapter by lazy { ViewPagerProductDetailAdapter() }
    private val colorsAdapter by lazy { ColorsAdapter() }
    private val sizesAdapter by lazy { SizesAdapter() }
    private var selectedColor: Int? = null
    private var selectedSize: String? = null
    private val cartViewModel by viewModels<AddToCartViewModel>()

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
        initClickListener(product)

        observeToAddToCartState()


    }

    private fun observeToAddToCartState() {
        lifecycleScope.launchWhenStarted {
            cartViewModel.addToCart.collect {
                when (it) {
                    is Resources.Error -> {
                        binding.productDetailBtnAddtocart.revertAnimation()
                        Toasty.error(requireContext(), it.message.toString(), Toast.LENGTH_LONG)
                            .show()
                    }
                    is Resources.Loading -> {
                        binding.productDetailBtnAddtocart.startAnimation()
                    }
                    is Resources.Success -> {
                        binding.productDetailBtnAddtocart.revertAnimation()
                        Toasty.success(
                            requireContext(),
                            getString(R.string.product_added_successfully),
                            Toast.LENGTH_LONG
                        ).show()

                    }
                    else -> Unit
                }
            }
        }
    }

    private fun initClickListener(product: Product) {
        sizesAdapter.onItemClick = {
            selectedSize = it
        }
        colorsAdapter.onItemClick = {
            selectedColor = it
        }

        binding.productDetailBtnAddtocart.setOnClickListener {
            cartViewModel.addUpdateProductInCart(
                CartProduct(
                    product,
                    1,
                    selectedColor,
                    selectedSize
                )
            )
        }

        binding.productDetailCloseIcon.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupData(product: Product) {
        binding.apply {
            productDetailProductName.text = product.name
            if (product.offerPercentage == null) {
                productDetailProductPrice.text = getString(R.string.dollar) + " ${
                    String.format(
                        Locale.getDefault(),
                        "%.2f",
                        product.price
                    )
                }"
            }

            product.offerPercentage?.let {
                val remainingPercentage = 1f - it
                val priceAfterDiscount = remainingPercentage * product.price
                productDetailProductPrice.text =
                    getString(R.string.dollar) + " ${
                        String.format(
                            Locale.getDefault(),
                            "%.2f",
                            priceAfterDiscount
                        )
                    }"
            }


            product.description?.let {
                productDetailProductDesc.text =it
            }
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














