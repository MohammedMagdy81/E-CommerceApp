package com.example.e_commerce.fragments.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce.adapters.BestProductsAdapter
import com.example.e_commerce.data.Categories
import com.example.e_commerce.databinding.FragmentBaseCategoryBinding
import com.example.e_commerce.fragments.shopping.HomeFragmentDirections
import com.example.e_commerce.mvvm.CategoriesViewModel
import com.example.e_commerce.mvvm.factory.BaseCategoryViewModelFactory
import com.example.e_commerce.utils.Resources
import com.example.e_commerce.utils.showBottomNav
import com.google.firebase.firestore.FirebaseFirestore
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject


open class BaseCategoryFragment(private val categories: Categories) : Fragment() {

    private lateinit var binding: FragmentBaseCategoryBinding

    protected val offerAdapter by lazy { BestProductsAdapter() }
    protected val bestProductsAdapter by lazy { BestProductsAdapter() }


    @Inject
    lateinit var firestore: FirebaseFirestore

    val viewModel by viewModels<CategoriesViewModel> {
        BaseCategoryViewModelFactory(firestore, categories)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBaseCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOfferRv()
        setupBestProductsRv()

        observeToOfferState()
        observeToBestProductsState()

        initClickListener()

        binding.rvOffer.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollHorizontally(1) && dx != 0) {

                }
            }
        })

        binding.baseCategoryScroll.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
                if (v.getChildAt(0).bottom <= v.height + scrollY) {

                }

            })
    }

    private fun initClickListener() {
        bestProductsAdapter.onItemClick = {
            val action = HomeFragmentDirections.actionHomeFragmentToProductDetailFragment(it)
            findNavController().navigate(action)
        }
        offerAdapter.onItemClick = {
            val action = HomeFragmentDirections.actionHomeFragmentToProductDetailFragment(it)
            findNavController().navigate(action)
        }
    }

    private fun observeToBestProductsState() {
        lifecycleScope.launchWhenStarted {
            viewModel.bestProducts.collectLatest {
                when (it) {
                    is Resources.Error -> {
                        binding.baseCategorySpinkit.visibility = View.GONE
                        Toasty.error(requireContext(), it.message.toString(), Toast.LENGTH_LONG)
                            .show()
                    }
                    is Resources.Loading -> {
                        binding.baseCategorySpinkit.visibility = View.VISIBLE
                    }
                    is Resources.Success -> {
                        binding.baseCategorySpinkit.visibility = View.GONE
                        bestProductsAdapter.differ.submitList(it.data)
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun observeToOfferState() {
        lifecycleScope.launchWhenStarted {
            viewModel.offerProducts.collectLatest {
                when (it) {
                    is Resources.Error -> {
                        binding.baseCategorySpinkit.visibility = View.GONE
                        Toasty.error(requireContext(), it.message.toString(), Toast.LENGTH_LONG)
                            .show()
                    }
                    is Resources.Loading -> {
                        binding.baseCategorySpinkit.visibility = View.VISIBLE
                    }
                    is Resources.Success -> {
                        binding.baseCategorySpinkit.visibility = View.GONE
                        offerAdapter.differ.submitList(it.data)
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun setupOfferRv() {
        binding.apply {
            rvOffer.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            rvOffer.adapter = offerAdapter
        }
    }

    private fun setupBestProductsRv() {
        binding.apply {
            rvBestProducts.layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            rvBestProducts.adapter = bestProductsAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        showBottomNav()
    }

}