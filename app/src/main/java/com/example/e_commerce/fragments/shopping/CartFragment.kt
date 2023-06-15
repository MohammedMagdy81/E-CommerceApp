package com.example.e_commerce.fragments.shopping

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_commerce.R
import com.example.e_commerce.adapters.CartProductsAdapter
import com.example.e_commerce.databinding.FragmentCartBinding
import com.example.e_commerce.firebase.FirebaseCommon
import com.example.e_commerce.mvvm.CartViewModel
import com.example.e_commerce.utils.Resources
import com.example.e_commerce.utils.VerticalItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private val viewModel by activityViewModels<CartViewModel>()
    private val cartAdapter by lazy {
        CartProductsAdapter()
    }

    var totalPrice = 0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCartBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCartRv()
        collectCartState()
        collectTotalPriceState()
        initClickListener()
        collectDeleteDialogState()
    }

    private fun collectDeleteDialogState() {
        lifecycleScope.launchWhenStarted {
            viewModel.deleteDialog.collectLatest {
                val dialog = AlertDialog.Builder(requireContext()).apply {
                    setMessage(getString(R.string.are_u_want_to_delete))
                    setTitle(getString(R.string.delete_item))
                    setPositiveButton(getString(R.string.yes)) { dialog, i ->
                        dialog.dismiss()
                        viewModel.deleteCartProduct(it)

                    }
                    setNegativeButton(getString(R.string.cancel)) { dialog, i ->
                        dialog.dismiss()
                    }
                }
                dialog.create()
                dialog.show()
            }
        }
    }

    private fun initClickListener() {
        cartAdapter.onItemClick = {
            val action =
                CartFragmentDirections.actionCartFragmentToProductDetailFragment(it.product)
            findNavController().navigate(action)
        }
        cartAdapter.onItemPlusClick = {
            viewModel.changeQuantity(it, FirebaseCommon.QuantityChanging.INCREASE)
        }
        cartAdapter.onItemMinusClick = {
            viewModel.changeQuantity(it, FirebaseCommon.QuantityChanging.DECREASE)
        }
        binding.cartBtnCheckout.setOnClickListener {
            val action = CartFragmentDirections.actionCartFragmentToBillingFragment(
                totalPrice,
                cartAdapter.differ.currentList.toTypedArray()
            )
            findNavController().navigate(action)
        }
    }

    private fun collectTotalPriceState() {
        lifecycleScope.launchWhenStarted {
            viewModel.productsPrice.collectLatest { price ->
                price?.let {
                    totalPrice=price
                    binding.cartPriceTotal.text = "$ $price"
                }

            }
        }
    }

    private fun collectCartState() {
        lifecycleScope.launchWhenStarted {
            viewModel.cartProducts.collectLatest {
                when (it) {
                    is Resources.Error -> {
                        binding.cartProgress.visibility = View.INVISIBLE
                        Toasty.error(requireContext(), it.message.toString(), Toast.LENGTH_LONG)
                            .show()
                    }
                    is Resources.Loading -> {
                        binding.cartProgress.visibility = View.VISIBLE
                    }
                    is Resources.Success -> {
                        binding.cartProgress.visibility = View.INVISIBLE
                        if (it.data!!.isEmpty()) {
                            showEmptyLayout()
                            hideOtherViews()
                        } else {
                            hideEmptyLayout()
                            showOtherViews()
                            cartAdapter.differ.submitList(it.data)

                        }
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun hideOtherViews() {
        binding.apply {
            cartRv.visibility = View.GONE
            cartTotalContainer.visibility = View.GONE
            cartBtnCheckout.visibility = View.GONE
        }
    }

    private fun showOtherViews() {
        binding.apply {
            cartRv.visibility = View.VISIBLE
            cartTotalContainer.visibility = View.VISIBLE
            cartBtnCheckout.visibility = View.VISIBLE
        }
    }

    private fun hideEmptyLayout() {
        binding.cartEmptyLayout.visibility = View.GONE
    }

    private fun showEmptyLayout() {
        binding.cartEmptyLayout.visibility = View.VISIBLE
    }

    private fun setupCartRv() {
        binding.cartRv.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = cartAdapter
            addItemDecoration(VerticalItemDecoration())
        }
    }


}