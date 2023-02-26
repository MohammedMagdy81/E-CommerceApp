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
import com.example.e_commerce.data.Address
import com.example.e_commerce.databinding.FragmentAddressBinding
import com.example.e_commerce.mvvm.AddressViewModel
import com.example.e_commerce.utils.Resources
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class AddressFragment : Fragment() {

    private lateinit var binding: FragmentAddressBinding
    private val viewModel by viewModels<AddressViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddressBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            addressBtnSave.setOnClickListener {
                val address = Address(
                    addressLocationEt.text.toString(),
                    addressFullNameEt.text.toString(),
                    addressPhoneEt.text.toString(),
                    addressStreetEt.text.toString(),
                    addressCityEt.text.toString(),
                    addressStateEt.text.toString()
                )
                viewModel.addAddress(address)

            }
        }
        collectAddAddressState()
        collectErrorState()
    }

    private fun collectErrorState() {
        lifecycleScope.launchWhenStarted {
            viewModel.errorMessage.collectLatest {
                Toasty.error(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun collectAddAddressState() {
        lifecycleScope.launchWhenStarted {
            viewModel.addNewAddress.collectLatest {
                when (it) {
                    is Resources.Loading -> {
                        binding.addressProgress.visibility = View.VISIBLE
                    }
                    is Resources.Error -> {
                        binding.addressProgress.visibility = View.GONE
                        Toasty.error(requireContext(), it.message.toString(), Toast.LENGTH_LONG)
                            .show()
                    }
                    is Resources.Success -> {
                        binding.addressProgress.visibility = View.GONE
                        findNavController().navigateUp()
                    }
                    else -> Unit
                }

            }
        }
    }
}










