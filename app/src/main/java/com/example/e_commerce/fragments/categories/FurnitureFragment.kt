package com.example.e_commerce.fragments.categories

import android.os.Bundle
import android.view.View
import com.example.e_commerce.data.Categories
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FurnitureFragment : BaseCategoryFragment(Categories.Furniture) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}