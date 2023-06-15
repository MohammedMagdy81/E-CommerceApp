package com.example.e_commerce.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.Product
import com.example.e_commerce.utils.Constants.BEST_DEAL
import com.example.e_commerce.utils.Constants.BEST_PRODUCTS
import com.example.e_commerce.utils.Constants.CATEGORY
import com.example.e_commerce.utils.Constants.ID
import com.example.e_commerce.utils.Constants.PRODUCTS_COLLECTION
import com.example.e_commerce.utils.Constants.SPECIAL_PRODUCTS
import com.example.e_commerce.utils.Resources
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _specialProducts = MutableStateFlow<Resources<List<Product>>>(Resources.Ideal())
    val specialProducts = _specialProducts.asStateFlow()

    private val _bestDealProducts = MutableStateFlow<Resources<List<Product>>>(Resources.Ideal())
    val bestDealProducts = _bestDealProducts.asStateFlow()

    private val _bestProducts = MutableStateFlow<Resources<List<Product>>>(Resources.Ideal())
    val bestProducts = _bestProducts.asStateFlow()

    private val pagingInfo = PagingInfo()


    init {
        getSpecialProducts()
        getBestDealProducts()
        getBestProducts()
    }

     fun getSpecialProducts() {
        runBlocking {
            _specialProducts.emit(Resources.Loading())
        }
        firestore.collection(PRODUCTS_COLLECTION).whereEqualTo(CATEGORY, SPECIAL_PRODUCTS)
            .get()
            .addOnSuccessListener {
                val products = it.toObjects(Product::class.java)
                viewModelScope.launch {
                    _specialProducts.emit(Resources.Success(products))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _specialProducts.emit(Resources.Error(it.message.toString()))
                }
            }

    }

     fun getBestDealProducts() {
        runBlocking {
            _bestDealProducts.emit(Resources.Loading())
        }
        firestore.collection(PRODUCTS_COLLECTION)
            .whereEqualTo(CATEGORY, BEST_DEAL)
            .get()
            .addOnSuccessListener {
                val products = it.toObjects(Product::class.java)
                viewModelScope.launch {
                    _bestDealProducts.emit(Resources.Success(products))
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _bestDealProducts.emit(Resources.Error(it.message.toString()))
                }
            }
    }

    fun getBestProducts() {
        if (!pagingInfo.isPagingEnd) {
            runBlocking {
                _bestProducts.emit(Resources.Loading())
            }
            firestore.collection(PRODUCTS_COLLECTION)
                .limit(pagingInfo.page * 6)
                .whereEqualTo(CATEGORY, BEST_PRODUCTS)
                .get()
                .addOnSuccessListener {
                    val products = it.toObjects(Product::class.java)
                    pagingInfo.isPagingEnd = products == pagingInfo.oldProductsList
                    pagingInfo.oldProductsList = products
                    viewModelScope.launch {
                        _bestProducts.emit(Resources.Success(products))
                    }

                    pagingInfo.page++

                }.addOnFailureListener {
                    viewModelScope.launch {
                        _bestProducts.emit(Resources.Error(it.message.toString()))
                    }
                }
        }
    }
}

internal data class PagingInfo(
    var page: Long = 1,
    var oldProductsList: List<Product> = emptyList(),
    var isPagingEnd: Boolean = false
)