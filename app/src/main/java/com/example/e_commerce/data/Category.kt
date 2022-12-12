package com.example.e_commerce.data

sealed class Category(val category: String) {
    object Sports : Category("Sports")
    object Shoes : Category("Shoes")
    object Furniture : Category("Furniture")
    object Clothes : Category("Clothes")
    object Accessory : Category("Accessory")
}

