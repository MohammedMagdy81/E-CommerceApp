package com.example.e_commerce.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.Product
import com.example.e_commerce.utils.Constants.PRODUCTS_COLLECTION
import com.example.e_commerce.utils.Resources
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class SearchViewModel : ViewModel() {

    private val productsCollection = Firebase.firestore.collection(PRODUCTS_COLLECTION)

    private val _searchProducts = MutableStateFlow<Resources<List<Product>>>(Resources.Ideal())
    val searchProducts = _searchProducts.asStateFlow()


    fun searchProducts(searchQuery: String) {
        runBlocking {
            _searchProducts.emit(Resources.Loading())
        }
        productsCollection
            .orderBy("name")
            .startAt(searchQuery)
            .endAt(searchQuery.trim() + "\uf8ff")

            .get().addOnSuccessListener {
                val products = it.toObjects(Product::class.java)
                viewModelScope.launch {
                    _searchProducts.emit(Resources.Success(products))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _searchProducts.emit(Resources.Error(it.localizedMessage))
                }
            }
    }
}