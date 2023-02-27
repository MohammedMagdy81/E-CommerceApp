package com.example.e_commerce.fragments.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_commerce.R
import com.example.e_commerce.adapters.OrdersAdapter
import com.example.e_commerce.databinding.FragmentOrdersBinding
import com.example.e_commerce.mvvm.OrdersViewModel
import com.example.e_commerce.utils.Resources
import com.example.e_commerce.utils.VerticalItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.flow.collectLatest


@AndroidEntryPoint
class OrdersFragment : Fragment() {

    private lateinit var binding: FragmentOrdersBinding
    private val viewModel by viewModels<OrdersViewModel>()
    private val ordersAdapter by lazy { OrdersAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOrdersBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupOrdersRv()
        collectOrdersState()
    }

    private fun collectOrdersState() {
        lifecycleScope.launchWhenStarted {
            viewModel.allOrders.collectLatest {
                when (it) {

                    is Resources.Error -> {
                        binding.ordersSpinKit.visibility = View.GONE
                        Toasty.error(requireContext(), it.message.toString(), Toast.LENGTH_LONG)
                            .show()
                    }
                    is Resources.Loading -> {
                        binding.ordersSpinKit.visibility = View.VISIBLE
                    }
                    is Resources.Success -> {
                        binding.ordersSpinKit.visibility = View.GONE
                        ordersAdapter.differList.submitList(it.data)

                    }
                    else -> Unit
                }
            }
        }
    }

    private fun setupOrdersRv() {
        binding.ordersRv.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(VerticalItemDecoration(10))
            adapter = ordersAdapter
        }
    }

}