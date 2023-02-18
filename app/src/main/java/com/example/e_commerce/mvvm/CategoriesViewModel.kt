package com.example.e_commerce.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.Category
import com.example.e_commerce.data.Product
import com.example.e_commerce.utils.Constants.CATEGORY
import com.example.e_commerce.utils.Constants.OFFER_PERCENTAGE
import com.example.e_commerce.utils.Constants.PRODUCTS_COLLECTION
import com.example.e_commerce.utils.Resources
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class CategoriesViewModel constructor(
    private val firestore: FirebaseFirestore,
    private val category: Category
) : ViewModel() {

    private val _offerProducts = MutableStateFlow<Resources<List<Product>>>(Resources.Ideal())
    val offerProducts = _offerProducts.asStateFlow()

    private val _bestProducts = MutableStateFlow<Resources<List<Product>>>(Resources.Ideal())
    val bestProducts = _bestProducts.asStateFlow()

    init {
        getOfferProducts()
        getBestProducts()
    }

    fun getOfferProducts() {
        runBlocking {
            _offerProducts.emit(Resources.Loading())
        }
        firestore.collection(PRODUCTS_COLLECTION).whereEqualTo(CATEGORY, category.category)
            .whereNotEqualTo(OFFER_PERCENTAGE, null)
            .get()
            .addOnSuccessListener { result ->
                val offerProducts = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _offerProducts.emit(Resources.Success(offerProducts))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _offerProducts.emit(Resources.Error(it.message.toString()))
                }
            }
    }

    fun getBestProducts() {
        runBlocking {
            _bestProducts.emit(Resources.Loading())
        }
        firestore.collection(PRODUCTS_COLLECTION)
            .whereEqualTo(CATEGORY, category.category)
            .whereEqualTo(OFFER_PERCENTAGE, null)
            .get()
            .addOnSuccessListener { result ->
                val offerProducts = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _bestProducts.emit(Resources.Success(offerProducts))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _bestProducts.emit(Resources.Error(it.message.toString()))
                }
            }
    }

}