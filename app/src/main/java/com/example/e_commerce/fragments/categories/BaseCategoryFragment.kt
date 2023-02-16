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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce.R
import com.example.e_commerce.adapters.BestProductsAdapter
import com.example.e_commerce.data.Category
import com.example.e_commerce.databinding.FragmentBaseCategoryBinding
import com.example.e_commerce.mvvm.CategoriesViewModel
import com.example.e_commerce.mvvm.factory.BaseCategoryViewModelFactory
import com.example.e_commerce.utils.Resources
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import javax.inject.Inject


open class BaseCategoryFragment(private val category: Category)
 : Fragment() {

    private lateinit var binding: FragmentBaseCategoryBinding
    protected val offerAdapter by lazy { BestProductsAdapter() }
    protected val bestProductsAdapter by lazy { BestProductsAdapter() }



    @Inject
    lateinit var firestore :FirebaseFirestore

    val viewModel by viewModels<CategoriesViewModel> {
        BaseCategoryViewModelFactory(firestore,category )
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

        binding.rvOffer.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollHorizontally(1) && dx != 0) {
                    onPagingOfferRequest()
                }
            }
        })

        binding.baseCategoryScroll.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
                if (v.getChildAt(0).bottom <= v.height + scrollY) {
                    onPagingBestProductRequest()
                }

            })
    }

    private fun observeToBestProductsState() {
        lifecycleScope.launchWhenStarted {
            viewModel.bestProducts.collect {
                when (it) {
                    is Resources.Error -> {
                        binding.baseCategorySpinkit.visibility = View.GONE
                        Toasty.error(requireContext(), "error happen !", Toast.LENGTH_LONG).show()
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
            viewModel.offerProducts.collect {
                when (it) {
                    is Resources.Error -> {
                        binding.baseCategorySpinkit.visibility = View.GONE
                        Toasty.error(requireContext(), "error happen !", Toast.LENGTH_LONG).show()
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

    open fun onPagingOfferRequest() {}
    open fun onPagingBestProductRequest() {}

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

}