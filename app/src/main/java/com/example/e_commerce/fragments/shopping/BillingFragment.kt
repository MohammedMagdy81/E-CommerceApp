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
import com.example.e_commerce.adapters.AddressAdapter
import com.example.e_commerce.adapters.BillingAdapter
import com.example.e_commerce.databinding.FragmentBillingBinding
import com.example.e_commerce.mvvm.BillingViewModel
import com.example.e_commerce.utils.HorizontalItemDecoration
import com.example.e_commerce.utils.Resources
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class BillingFragment : Fragment() {

    private lateinit var binding: FragmentBillingBinding
    private val addressAdapter by lazy { AddressAdapter() }
    private val billingProductAdapter by lazy { BillingAdapter() }

    private val args by navArgs<BillingFragmentArgs>()

    private val viewModel by viewModels<BillingViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBillingBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAddressRv()
        setupBillingProductRv()
        collectAddressesState()
        initClickListener()
        binding.billingPriceTotal.text = args.totalPrice.toString()
        billingProductAdapter.differList.submitList(args.products.toList())

    }

    private fun initClickListener() {
        binding.addAddressIcon.setOnClickListener {
            findNavController().navigate(R.id.action_billingFragment_to_addressFragment)
        }
    }

    private fun collectAddressesState() {
        lifecycleScope.launchWhenStarted {
            viewModel.address.collectLatest {
                when (it) {
                    is Resources.Error -> {
                        binding.billingProgress.visibility = View.GONE
                        Toasty.error(requireContext(), it.message.toString(), Toast.LENGTH_LONG)
                            .show()
                    }
                    is Resources.Loading -> {
                        binding.billingProgress.visibility = View.VISIBLE
                    }
                    is Resources.Success -> {
                        binding.billingProgress.visibility = View.GONE
                        addressAdapter.differList.submitList(it.data)
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun setupBillingProductRv() {
        binding.billingProductsRv.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = billingProductAdapter
            addItemDecoration(HorizontalItemDecoration())
        }
    }

    private fun setupAddressRv() {
        binding.billingAddressRv.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = addressAdapter
            addItemDecoration(HorizontalItemDecoration())
        }
    }
}