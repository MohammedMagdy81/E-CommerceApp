package com.example.e_commerce.utils

fun Float?.getProductPrice(price: Float): Float {
    if (this == null)
        return price

    val remainingPercentage = 1f - this
    val priceAfterOffer = remainingPercentage * price

    return priceAfterOffer
}