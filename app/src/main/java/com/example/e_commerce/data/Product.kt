package com.example.e_commerce.data

data class Product(
    val id: String,
    val name: String,
    val category: String,
    val desc: String,
    val price: Float,
    val offerPercentage: Float? = null,
    val colors: List<Int>? = null,
    val sizes: List<String>? = null,
    val images: List<String>
) {
    constructor() : this("0", "", "", "", 0f, images = emptyList())
}