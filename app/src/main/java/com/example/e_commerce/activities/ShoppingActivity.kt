package com.example.e_commerce.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.e_commerce.R
import com.example.e_commerce.databinding.ActivityShoppingBinding

class ShoppingActivity : AppCompatActivity() {

    private lateinit var binding : ActivityShoppingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityShoppingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController=  findNavController(R.id.shoppingHostFragment)
        binding.bottomNavigationMenu.setupWithNavController(navController)

    }
}