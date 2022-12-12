package com.example.e_commerce.fragments.shopping

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.e_commerce.R
import com.example.e_commerce.adapters.HomeViewPagerAdapter
import com.example.e_commerce.databinding.FragmentHomeBinding
import com.example.e_commerce.fragments.categories.*
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment(R.layout.fragment_home) {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewPagerAdapter: HomeViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoriesFragment = arrayListOf<Fragment>(
            MainCategoriesFragment(),
            SportsFragment(),
            ShoesFragment(),
            ClothesFragment(),
            AccessoryFragment(),
            FurnitureFragment()
        )
        binding.homeViewPager.isUserInputEnabled=false

        homeViewPagerAdapter =
            HomeViewPagerAdapter(categoriesFragment, childFragmentManager, lifecycle)
        binding.homeViewPager.adapter = homeViewPagerAdapter
        TabLayoutMediator(binding.homeTabLayout, binding.homeViewPager) { tab, position ->
            when (position) {
                0 -> tab.text = "Home"
                1 -> tab.text = "Clothes"
                2 -> tab.text = "Shoes"
                3 -> tab.text = "Sports"
                4 -> tab.text = "Accessory"
                5 -> tab.text = "Furniture"


            }
        }.attach()


    }
}