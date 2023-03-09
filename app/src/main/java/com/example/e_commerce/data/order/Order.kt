package com.example.e_commerce.data.order

import android.os.Parcelable
import com.example.e_commerce.data.Address
import com.example.e_commerce.data.CartProduct
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random.Default.nextLong

@Parcelize
data class Order(
    val status: String,
    val totalPrice: Float,
    val products: List<CartProduct>,
    val address: Address,
    val date: String = SimpleDateFormat("yyyy - MM - dd", Locale.ENGLISH).format(Date()),
    val orderId: Long = nextLong(0, 100_000_000) + totalPrice.toLong()
) : Parcelable {
    constructor() : this("", 0f, emptyList(), Address(), "", 1L)
}
