package com.example.e_commerce.fragments.categories

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.e_commerce.data.Category
import com.example.e_commerce.mvvm.CategoriesViewModel
import com.example.e_commerce.mvvm.factory.BaseCategoryViewModelFactory
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AccessoriesFragment : BaseCategoryFragment(Category.Accessory) {
//    @Inject
//    private lateinit var firestore: FirebaseFirestore
//    val viewModel by viewModels<CategoriesViewModel> {
//        BaseCategoryViewModelFactory(firestore, Category.Accessory)
//    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}