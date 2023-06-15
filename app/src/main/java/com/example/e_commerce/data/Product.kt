package com.example.e_commerce.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: String,
    val name: String,
    val category: String,
    val description: String,
    val price: Float,
    val offerPercentage: Float? = null,
    val colors: List<Int>? = null,
    val sizes: List<String>? = null,
    val images: List<String>
) : Parcelable {
    constructor() : this("0", "", "", "", 0f, images = emptyList())
}