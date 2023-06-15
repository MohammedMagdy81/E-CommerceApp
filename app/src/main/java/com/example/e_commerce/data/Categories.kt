package com.example.e_commerce.data

sealed class Categories(val category: String) {
    object Shoes : Categories("Shoes")
    object Sports : Categories("Sports")
    object Furniture : Categories("Furniture")
    object Accessory : Categories("Accessory")
    object Food : Categories("Food")
    object Coffee : Categories("Coffee")

}
