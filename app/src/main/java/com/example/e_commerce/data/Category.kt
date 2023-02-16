package com.example.e_commerce.data

sealed class Category(val category: String) {
    object Shoes : Category("Shoes")
    object Sports : Category("Sports")
    object Furniture : Category("Furniture")
    object Accessory : Category("Accessory")
}
