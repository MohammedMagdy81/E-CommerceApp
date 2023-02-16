package com.example.e_commerce.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.e_commerce.databinding.FragmentMainCategoryBinding

class HomeTabsAdapter(
    lifecycle: Lifecycle,
    fm: FragmentManager,
    private val fragments: List<Fragment>
) : FragmentStateAdapter(fm, lifecycle) {
    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]

}