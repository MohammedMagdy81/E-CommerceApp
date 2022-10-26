package com.example.e_commerce.fragments.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_commerce.R
import com.example.e_commerce.adapters.BestDealsAdapter
import com.example.e_commerce.adapters.BestProductAdapter
import com.example.e_commerce.adapters.SpecialProductAdapter
import com.example.e_commerce.databinding.FragmentMainCategoriesBinding
import com.example.e_commerce.mvvm.MainCategoryViewModel
import com.example.e_commerce.utils.Resources
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainCategoriesFragment : Fragment(R.layout.fragment_main_categories) {

    private lateinit var binding: FragmentMainCategoriesBinding
    private lateinit var specialProductAdapter: SpecialProductAdapter
    private lateinit var bestDealsAdapter: BestDealsAdapter
    private lateinit var bestProductAdapter: BestProductAdapter

    private val mainCategoryViewModel by viewModels<MainCategoryViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainCategoriesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRV()

        lifecycleScope.launchWhenStarted {
            launch {
                mainCategoryViewModel.specialProductState.collect {
                    when (it) {
                        is Resources.Error -> {
                            hideLoading()
                            Toast.makeText(
                                requireContext(),
                                it.message.toString(),
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }
                        is Resources.Loading -> {
                            showLoading()
                        }
                        is Resources.Success -> {
                            specialProductAdapter.differ.submitList(it.data)
                            hideLoading()
                        }
                    }
                }
            }

            launch {
                mainCategoryViewModel.bestDealProductState.collect {
                    when (it) {
                        is Resources.Error -> {
                            hideLoading()
                            Toast.makeText(
                                requireContext(),
                                it.message.toString(),
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }
                        is Resources.Loading -> {
                            showLoading()
                        }
                        is Resources.Success -> {
                            bestDealsAdapter.differ.submitList(it.data)
                            hideLoading()
                        }
                    }
                }

            }

            launch {
                mainCategoryViewModel.bestProductState.collectLatest {
                    when (it) {
                        is Resources.Error -> {
                            hideLoading()
                            Toast.makeText(
                                requireContext(),
                                it.message.toString(),
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        is Resources.Loading -> {
                            showLoading()
                        }
                        is Resources.Success -> {
                            bestProductAdapter.differ.submitList(it.data)
                            hideLoading()
                        }
                    }
                }
            }


        }


    }

    private fun showLoading() {
        binding.nainCategoryProgressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.nainCategoryProgressBar.visibility = View.INVISIBLE
    }

    private fun setUpRV() {
        specialProductAdapter = SpecialProductAdapter()
        bestDealsAdapter = BestDealsAdapter()
        bestProductAdapter = BestProductAdapter()

        binding.specialRv.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = specialProductAdapter

        }

        binding.bestDealProductsRv.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = bestDealsAdapter
        }

        binding.bestProductsRv.apply {
            layoutManager =
                GridLayoutManager(requireContext(), 2)
            adapter = bestProductAdapter
        }
    }
}





