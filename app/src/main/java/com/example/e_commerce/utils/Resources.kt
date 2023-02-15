package com.example.e_commerce.utils

sealed class Resources<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T?) : Resources<T>(data = data)
    class Error<T>(message: String?) : Resources<T>(message = message)
    class Loading<T>() : Resources<T>()
    class Ideal<T>():Resources<T>()
}