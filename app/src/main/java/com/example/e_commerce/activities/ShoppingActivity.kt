package com.example.e_commerce.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.e_commerce.R
import com.example.e_commerce.databinding.ActivityShoppingBinding
import com.example.e_commerce.mvvm.CartViewModel
import com.example.e_commerce.utils.Resources
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ShoppingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityShoppingBinding
    val viewModel by viewModels<CartViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShoppingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController = findNavController(R.id.shoppingNavHost)
        binding.bottomNavigationMenu.setupWithNavController(navController)

        lifecycleScope.launchWhenStarted {
            viewModel.cartProducts.collectLatest {
                when (it) {
                    is Resources.Success -> {
                        val productCount = it.data?.size ?: 0
                        val bottomNav =
                            findViewById<BottomNavigationView>(R.id.bottomNavigationMenu)
                        bottomNav.getOrCreateBadge(R.id.cartFragment).apply {
                            number = productCount
                            backgroundColor = resources.getColor(R.color.orange_color)
                        }
                    }
                    else -> Unit
                }
            }
        }

    }
}











