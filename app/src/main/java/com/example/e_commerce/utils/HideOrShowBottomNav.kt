package com.example.e_commerce.utils

import android.view.View
import androidx.fragment.app.Fragment
import com.example.e_commerce.R
import com.example.e_commerce.activities.ShoppingActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

fun Fragment.hideBottomNavigation() {
    val bottomNavigation =
        (activity as ShoppingActivity).findViewById<BottomNavigationView>(R.id.bottomNavigationMenu)
    bottomNavigation.visibility = View.INVISIBLE
}

fun Fragment.showBottomNavigation() {
    val bottomNavigation =
        (activity as ShoppingActivity).findViewById<BottomNavigationView>(R.id.bottomNavigationMenu)
    bottomNavigation.visibility = View.VISIBLE
}