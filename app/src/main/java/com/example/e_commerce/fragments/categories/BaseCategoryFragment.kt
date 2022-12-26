package com.example.e_commerce.fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce.R
import com.example.e_commerce.adapters.BestProductAdapter
import com.example.e_commerce.databinding.FragmentBaseCategoryBinding
import com.example.e_commerce.fragments.shopping.HomeFragmentDirections
import com.example.e_commerce.utils.showBottomNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
open class BaseCategoryFragment : Fragment(R.layout.fragment_base_category) {

    private lateinit var binding: FragmentBaseCategoryBinding
    protected val offerAdapter: BestProductAdapter by lazy {
        BestProductAdapter()
    }
    protected val bestProductAdapter: BestProductAdapter by lazy {
        BestProductAdapter()
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBaseCategoryBinding.inflate(inflater)
        return return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOfferRV()
        setupBestProductRV()
        bestProductAdapter.onClick = {
            val direction = HomeFragmentDirections.actionHomeFragmentToProductDetailsFragment(it)
            findNavController().navigate(direction)
        }
        offerAdapter.onClick = {
            val direction = HomeFragmentDirections.actionHomeFragmentToProductDetailsFragment(it)
            findNavController().navigate(direction)
        }
    }

    fun showOfferProgress() {
        binding.offerProgressbar.visibility = View.VISIBLE
    }

    fun hideOfferProgress() {
        binding.offerProgressbar.visibility = View.GONE
    }

    fun showBestProductsProgress() {
        binding.bestProductsProgressbar.visibility = View.VISIBLE
    }

    fun hideBestProductsProgress() {
        binding.bestProductsProgressbar.visibility = View.GONE
    }


    private fun setupBestProductRV() {
        binding.baseCategoryBestProductRv.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = bestProductAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupOfferRV() {
        binding.baseCategoryHorizontalRv.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = offerAdapter
            setHasFixedSize(true)
        }
    }

    override fun onResume() {
        super.onResume()
        showBottomNavigation()
    }
}







