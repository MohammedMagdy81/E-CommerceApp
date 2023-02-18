package com.example.e_commerce.utils

import android.view.View
import androidx.fragment.app.Fragment
import com.example.e_commerce.R
import com.example.e_commerce.activities.ShoppingActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

fun Fragment.hideBottomNav() {
    val bottomNavView =
        (activity as ShoppingActivity).findViewById<BottomNavigationView>(R.id.bottomNavigationMenu)
    bottomNavView.visibility = View.GONE
}

fun Fragment.showBottomNav() {
    val bottomNavView =
        (activity as ShoppingActivity).findViewById<BottomNavigationView>(R.id.bottomNavigationMenu)
    bottomNavView.visibility = View.VISIBLE
}