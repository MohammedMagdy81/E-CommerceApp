package com.example.e_commerce.fragments.categories

import android.os.Bundle
import android.view.View
import com.example.e_commerce.data.Categories
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccessoriesFragment : BaseCategoryFragment(Categories.Accessory) {
//    @Inject
//    private lateinit var firestore: FirebaseFirestore
//    val viewModel by viewModels<CategoriesViewModel> {
//        BaseCategoryViewModelFactory(firestore, Category.Accessory)
//    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}