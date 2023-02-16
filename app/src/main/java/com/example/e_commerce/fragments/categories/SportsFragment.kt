package com.example.e_commerce.fragments.categories

import android.os.Bundle
import android.view.View
import com.example.e_commerce.data.Category
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SportsFragment : BaseCategoryFragment(Category.Sports) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}