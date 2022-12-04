package com.example.e_commerce.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.Product
import com.example.e_commerce.utils.Constants.BEST_CATEGORY_VALUE
import com.example.e_commerce.utils.Constants.CATEGORY_FIELD
import com.example.e_commerce.utils.Constants.PRODUCTS_COLLECTION
import com.example.e_commerce.utils.Resources
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _specialProductState = MutableStateFlow<Resources<List<Product>>>(Resources.Ideal())
    val specialProductState: StateFlow<Resources<List<Product>>> = _specialProductState

    private val _bestDealProductState =
        MutableStateFlow<Resources<List<Product>>>(Resources.Ideal())
    val bestDealProductState: StateFlow<Resources<List<Product>>> = _bestDealProductState

    private val _bestProductState = MutableStateFlow<Resources<List<Product>>>(Resources.Ideal())
    val bestProductState: StateFlow<Resources<List<Product>>> = _bestProductState


    private var pagingInfo = PagingInfo()

    init {
        fetchBestDealProduct()
        fetchSpecialProducts()
        fetchBestProduct()
    }

    private fun fetchSpecialProducts() {

        viewModelScope.launch {
            _specialProductState.emit(Resources.Loading())
        }
        firestore.collection("Products").whereEqualTo("category", "Special Product").get()
            .addOnSuccessListener { result ->
                val productsList = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _specialProductState.emit(Resources.Success(productsList))
                }
            }
            .addOnFailureListener {

                viewModelScope.launch {
                    _specialProductState.emit(Resources.Error(it.message.toString()))
                }
            }
    }

    private fun fetchBestDealProduct() {
        viewModelScope.launch {
            _bestDealProductState.emit(Resources.Loading())
        }
        firestore.collection("Products").whereEqualTo("category", "Best Deal").get()
            .addOnSuccessListener { result ->
                val productsList = result.toObjects(Product::class.java)
                viewModelScope.launch {
                    _bestDealProductState.emit(Resources.Success(productsList))
                }
            }
            .addOnFailureListener {

                viewModelScope.launch {
                    _bestDealProductState.emit(Resources.Error(it.message.toString()))
                }
            }
    }

    fun fetchBestProduct() {

        if (!pagingInfo.isPagingEnd) {
            viewModelScope.launch {
                _bestProductState.emit(Resources.Loading())
            }
            firestore.collection(PRODUCTS_COLLECTION).limit(pagingInfo.bestProductPage * 6)
                .whereEqualTo(CATEGORY_FIELD, BEST_CATEGORY_VALUE).orderBy("id", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener {
                    val resultList = it.toObjects(Product::class.java)

                    pagingInfo.isPagingEnd = resultList == pagingInfo.oldBestProducts
                    pagingInfo.oldBestProducts = resultList

                    viewModelScope.launch {
                        _bestProductState.emit(Resources.Success(resultList))
                    }
                    pagingInfo.bestProductPage++
                }
                .addOnFailureListener {
                    viewModelScope.launch {
                        _bestProductState.emit(
                            Resources.Error(
                                it.localizedMessage?.toString() ?: "An unknown Error !"
                            )
                        )
                    }
                }
        }
    }
}

internal data class PagingInfo(
    var bestProductPage: Long = 1,
    var oldBestProducts: List<Product> = emptyList(),
    var isPagingEnd: Boolean = false

)
















