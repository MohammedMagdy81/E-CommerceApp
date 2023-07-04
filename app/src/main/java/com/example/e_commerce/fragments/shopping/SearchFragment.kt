package com.example.e_commerce.fragments.shopping

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_commerce.adapters.BestDealProductsAdapter
import com.example.e_commerce.databinding.FragmentSearchBinding
import com.example.e_commerce.mvvm.SearchViewModel
import com.example.e_commerce.utils.Resources
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.*


@AndroidEntryPoint
class SearchFragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private val searchViewModel by viewModels<SearchViewModel>()

    private lateinit var productsAdapter: BestDealProductsAdapter
    private lateinit var inputMethodManger: InputMethodManager


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpSearchProductAdapter()
        showKeyboardAutomatically()
        searchProducts()
        productsAdapter.onItemClick = {
            val action = SearchFragmentDirections.actionSearchFragmentToProductDetailFragment(it)
            findNavController().navigate(action)
        }
        collectSearchState()
//        binding.edSearch.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//
//            }
//
//            override fun onTextChanged(letter: CharSequence?, p1: Int, p2: Int, p3: Int) {}
//
//            override fun afterTextChanged(p0: Editable?) {
//                if (p0.toString().isNotEmpty()) {
//                    searchViewModel.searchProducts(p0.toString())
//                } else {
//                    disableNoItemFound()
//                    productsAdapter.differ.submitList(emptyList())
//                    productsAdapter.notifyDataSetChanged()
//                }
//            }
//
//        })
    }

    var job: Job? = null
    private fun searchProducts() {
        binding.edSearch.addTextChangedListener { query ->
            if (query!!.isEmpty()) {
                productsAdapter.differ.submitList(emptyList())
            }
            val queryTrim = query.toString().trim()
            if (queryTrim.isNotEmpty()) {
                val searchQuery = query.toString().substring(0, 1).toUpperCase()
                    .plus(query.toString().substring(1))
                job?.cancel()
                job = CoroutineScope(Dispatchers.IO).launch {
                    delay(500L)
                    searchViewModel.searchProducts(searchQuery)
                }
            }
        }


    }

    private fun setUpSearchProductAdapter() {
        productsAdapter = BestDealProductsAdapter()
        binding.apply {
            rvSearch.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            rvSearch.adapter = productsAdapter
            rvSearch.setHasFixedSize(true)


        }
    }

    private fun collectSearchState() {
        lifecycleScope.launch {
            searchViewModel.searchProducts.collect {
                when (it) {
                    is Resources.Error -> {
                        binding.spinKit.visibility = View.GONE
                        Toasty.error(requireContext(), " ${it.message}", Toast.LENGTH_LONG).show()
                    }
                    is Resources.Loading -> {

                    }
                    is Resources.Success -> {
                        binding.spinKit.visibility = View.GONE
                        if (it.data!!.isEmpty()) {
                            showNoItemFound()
                        } else {
                            disableNoItemFound()
                            Log.d("SearchProduct", "${it.data}")
                            productsAdapter.differ.submitList(it.data)
                            productsAdapter.notifyDataSetChanged()

                        }


                    }
                }
            }
        }
    }

    private fun disableNoItemFound() {
        binding.apply {
            binding.tvNoItemFound.visibility = View.GONE
            binding.rvSearch.visibility = View.VISIBLE
        }
    }

    private fun showNoItemFound() {
        binding.tvNoItemFound.visibility = View.VISIBLE
        binding.rvSearch.visibility = View.GONE
    }

    private fun showKeyboardAutomatically() {
        inputMethodManger =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManger.toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )
        binding.edSearch.requestFocus()
    }

    private fun hideKeyboard() {
        val imm =
            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm!!.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

}













