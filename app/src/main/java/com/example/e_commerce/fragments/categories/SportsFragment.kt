package com.example.e_commerce.fragments.categories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.e_commerce.data.Category
import com.example.e_commerce.mvvm.CategoryViewModel
import com.example.e_commerce.mvvm.factory.BaseCategoryViewModelFactory
import com.example.e_commerce.utils.Resources
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SportsFragment : BaseCategoryFragment() {
    @Inject
    lateinit var firestore: FirebaseFirestore
    private val viewModel by viewModels<CategoryViewModel> {
        BaseCategoryViewModelFactory(firestore, Category.Sports)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launchWhenStarted {
            launch {
                viewModel.offerState.collect {
                    when (it) {
                        is Resources.Error -> {
                            Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_LONG)
                                .show()
                            hideOfferProgress()
                        }
                        is Resources.Loading -> {
                            showOfferProgress()
                        }
                        is Resources.Success -> {
                            offerAdapter.differ.submitList(it.data)
                            hideOfferProgress()
                        }
                        else -> Unit
                    }
                }
            }
            launch {
                viewModel.bestProductState.collectLatest {
                    when (it) {
                        is Resources.Error -> {
                            Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_LONG)
                                .show()
                            hideBestProductsProgress()
                        }
                        is Resources.Loading -> {
                            showBestProductsProgress()
                        }
                        is Resources.Success -> {
                            bestProductAdapter.differ.submitList(it.data)
                            hideBestProductsProgress()
                        }
                        else -> Unit
                    }
                }
            }


        }
    }
}