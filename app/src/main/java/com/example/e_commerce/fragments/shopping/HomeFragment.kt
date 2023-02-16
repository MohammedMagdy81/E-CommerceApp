package com.example.e_commerce.fragments.shopping

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.e_commerce.R
import com.example.e_commerce.adapters.HomeTabsAdapter
import com.example.e_commerce.databinding.FragmentHomeBinding
import com.example.e_commerce.fragments.categories.*
import com.google.android.material.tabs.TabLayoutMediator


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpTabsWithViewPager()
    }

    private fun setUpTabsWithViewPager() {
        val categories = arrayListOf<Fragment>(
            MainCategoryFragment(),
            SportsFragment(),
            ShoesFragment(),
            FurnitureFragment(),
            AccessoriesFragment()
        )
        binding.homeViewPager.isUserInputEnabled = false
        val tabsAdapter = HomeTabsAdapter(lifecycle, childFragmentManager, categories)
        binding.homeViewPager.adapter = tabsAdapter

        TabLayoutMediator(binding.homeTablayout, binding.homeViewPager) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.tab_home)
                1 -> tab.text = getString(R.string.sports)
                2 -> tab.text = getString(R.string.shoes)
                3 -> tab.text = getString(R.string.furniture)
                4 -> tab.text = getString(R.string.accessories)
            }

        }.attach()
    }


}