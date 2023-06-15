package com.example.e_commerce.fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_commerce.R
import com.example.e_commerce.adapters.BestDealProductsAdapter
import com.example.e_commerce.adapters.BestProductsAdapter
import com.example.e_commerce.adapters.SpecialProductsAdapter
import com.example.e_commerce.databinding.FragmentMainCategoryBinding
import com.example.e_commerce.fragments.shopping.HomeFragmentDirections
import com.example.e_commerce.mvvm.MainCategoryViewModel
import com.example.e_commerce.utils.Resources
import com.example.e_commerce.utils.showBottomNav
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainCategoryFragment : Fragment() {

    private lateinit var binding: FragmentMainCategoryBinding
    private lateinit var specialProductAdapter: SpecialProductsAdapter
    private lateinit var bestDealProductsAdapter: BestDealProductsAdapter
    private lateinit var bestProductsAdapter: BestProductsAdapter

    private val viewModel by viewModels<MainCategoryViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMainCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSpecialProductAdapter()
        setUpBestDealProductsAdapter()
        setupBestProductAdapter()
        observeToSpecialProducts()

        initClickOnProduct()

        binding.mainCategoryNestedScroll.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
                if (v.getChildAt(0).bottom <= v.height + scrollY) {
                    viewModel.getBestProducts()
                }

            })
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.getSpecialProducts()
            viewModel.getBestDealProducts()
            viewModel.getBestProducts()
            binding.swipeRefresh.isRefreshing = false
        }
    }

    private fun initClickOnProduct() {
        specialProductAdapter.onItemClick = {
            val action = HomeFragmentDirections.actionHomeFragmentToProductDetailFragment(it)
            findNavController().navigate(action)
        }
        bestDealProductsAdapter.onItemClick = {
            val action = HomeFragmentDirections.actionHomeFragmentToProductDetailFragment(it)
            findNavController().navigate(action)
        }
        bestProductsAdapter.onItemClick = {
            val action = HomeFragmentDirections.actionHomeFragmentToProductDetailFragment(it)
            findNavController().navigate(action)
        }
    }

    private fun setUpBestDealProductsAdapter() {
        bestDealProductsAdapter = BestDealProductsAdapter()
        binding.apply {
            rvBestDeal.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvBestDeal.adapter = bestDealProductsAdapter
        }
    }

    private fun setupBestProductAdapter() {
        bestProductsAdapter = BestProductsAdapter()
        binding.apply {
            rvBestProducts.layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            rvBestProducts.adapter = bestProductsAdapter
        }
    }

    private fun observeToSpecialProducts() {
        lifecycleScope.launchWhenStarted {
            viewModel.specialProducts.collectLatest {
                when (it) {
                    is Resources.Error -> {
                        binding.mainCategorySpinKit.visibility = View.GONE
                        Toasty.error(
                            requireContext(), "حدث خطأ ربما انقطع الاتصال بالانترنت !",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    is Resources.Loading -> {
                        binding.mainCategorySpinKit.visibility = View.VISIBLE
                    }
                    is Resources.Success -> {
                        binding.mainCategorySpinKit.visibility = View.GONE
                        specialProductAdapter.differ.submitList(it.data)
                    }
                    else -> Unit
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.bestDealProducts.collectLatest {
                when (it) {
                    is Resources.Error -> {
                        binding.mainCategorySpinKit.visibility = View.GONE
                        Toasty.error(
                            requireContext(), "عفوا لقد حدث خطأ !",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    is Resources.Loading -> {
                        binding.mainCategorySpinKit.visibility = View.VISIBLE
                    }
                    is Resources.Success -> {
                        binding.mainCategorySpinKit.visibility = View.GONE
                        bestDealProductsAdapter.differ.submitList(it.data)
                    }
                    else -> Unit
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.bestProducts.collectLatest {

                when (it) {
                    is Resources.Error -> {
                        binding.mainCategorySpinKit.visibility = View.GONE
                        Toasty.error(
                            requireContext(), "حدث خطأ تأكد من الاتصال بالنترنت !",
                            Toast.LENGTH_LONG
                        ).show()
                        binding.bestProductProgress.visibility = View.GONE
                    }
                    is Resources.Loading -> {
                        binding.mainCategorySpinKit.visibility = View.VISIBLE
                        binding.bestProductProgress.visibility = View.VISIBLE
                    }
                    is Resources.Success -> {
                        binding.mainCategorySpinKit.visibility = View.GONE
                        bestProductsAdapter.differ.submitList(it.data)
                        binding.bestProductProgress.visibility = View.GONE
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun setupSpecialProductAdapter() {
        specialProductAdapter = SpecialProductsAdapter()
        binding.apply {
            rvSpecialProduct.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvSpecialProduct.adapter = specialProductAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        showBottomNav()
    }
}