package com.example.e_commerce.fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_commerce.R
import com.example.e_commerce.adapters.BestProductAdapter
import com.example.e_commerce.databinding.FragmentBaseCategoryBinding

open class BaseCategoryFragment : Fragment(R.layout.fragment_base_category) {

    private lateinit var binding: FragmentBaseCategoryBinding
    private lateinit var offerAdapter: BestProductAdapter
    private lateinit var bestProductAdapter: BestProductAdapter

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
    }

    private fun setupBestProductRV() {
        bestProductAdapter = BestProductAdapter()
        binding.baseCategoryBestProductRv.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 2, GridLayoutManager.VERTICAL, false)
            adapter = bestProductAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupOfferRV() {
        offerAdapter = BestProductAdapter()
        binding.baseCategoryHorizontalRv.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = offerAdapter
            setHasFixedSize(true)
        }
    }
}







