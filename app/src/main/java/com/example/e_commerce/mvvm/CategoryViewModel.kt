package com.example.e_commerce.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.Category
import com.example.e_commerce.data.Product
import com.example.e_commerce.utils.Constants
import com.example.e_commerce.utils.Resources
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject


class CategoryViewModel constructor(
    private val firestore: FirebaseFirestore,
    private val category: Category
) : ViewModel() {
    private val _offerState = MutableStateFlow<Resources<List<Product>>>(Resources.Ideal())
    val offerState = _offerState.asStateFlow()

    private val _bestProductState = MutableStateFlow<Resources<List<Product>>>(Resources.Ideal())
    val bestProductState = _bestProductState.asStateFlow()

    init {
        fetchBestProducts()
        fetchOfferProducts()
    }

    fun fetchOfferProducts() {
        runBlocking {
            _offerState.emit(Resources.Loading())
        }
        firestore.collection(Constants.PRODUCTS_COLLECTION)
            .whereEqualTo(Constants.CATEGORY_FIELD, category.category)
            .whereNotEqualTo("offerPercentage", null).get()
            .addOnSuccessListener {
                val products = it.toObjects(Product::class.java)
                viewModelScope.launch {
                    _offerState.emit(Resources.Success(products))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _offerState.emit(Resources.Error(it.localizedMessage ?: "error happen!"))
                }
            }
    }

    fun fetchBestProducts() {
        runBlocking {
            _bestProductState.emit(Resources.Loading())
        }
        firestore.collection(Constants.PRODUCTS_COLLECTION)
            .whereEqualTo(Constants.CATEGORY_FIELD, category.category)
            .whereEqualTo("offerPercentage", null).get()
            .addOnSuccessListener {
                val products = it.toObjects(Product::class.java)
                viewModelScope.launch {
                    _bestProductState.emit(Resources.Success(products))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _bestProductState.emit(Resources.Error(it.localizedMessage ?: "error happen!"))
                }
            }
    }
}










