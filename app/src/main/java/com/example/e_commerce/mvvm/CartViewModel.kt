package com.example.e_commerce.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.CartProduct
import com.example.e_commerce.firebase.FirebaseCommon
import com.example.e_commerce.utils.Resources
import com.example.e_commerce.utils.getProductPrice
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val firebaseCommon: FirebaseCommon
) : ViewModel() {

    private var cartProductDocuments = emptyList<DocumentSnapshot>()

    private val _cartProducts = MutableStateFlow<Resources<List<CartProduct>>>(Resources.Ideal())
    val cartProducts = _cartProducts.asStateFlow()

    val productsPrice = cartProducts.map {
        when (it) {
            is Resources.Success -> {
                calculatePrice(it.data!!)
            }
            else -> null
        }
    }

    private val _deleteDialog = MutableSharedFlow<CartProduct>()
    val deleteDialog = _deleteDialog.asSharedFlow()
    private fun calculatePrice(cartProducts: List<CartProduct>): Float {
        return cartProducts.sumByDouble { cartProduct ->
            (cartProduct.product.offerPercentage.getProductPrice(cartProduct.product.price) * cartProduct.quantity).toDouble()
        }.toFloat()

    }

    init {
        getCartProducts()
    }

    private fun getCartProducts() {
        viewModelScope.launch { _cartProducts.emit(Resources.Loading()) }
        firestore.collection("users").document(auth.uid!!).collection("cart")
            .addSnapshotListener { value, error ->
                if (error != null || value == null) {
                    viewModelScope.launch {
                        _cartProducts.emit(Resources.Error(error?.message))
                    }
                } else {
                    val cartProducts = value.toObjects(CartProduct::class.java)
                    cartProductDocuments = value.documents
                    viewModelScope.launch {
                        _cartProducts.emit(Resources.Success(cartProducts))
                    }
                }
            }
    }


    fun changeQuantity(
        cartProduct: CartProduct,
        quantityChanging: FirebaseCommon.QuantityChanging
    ) {
        val index = cartProducts.value.data?.indexOf(cartProduct)
        if (index != null && index != -1) {
            val cartProductDocument = cartProductDocuments[index].id
            when (quantityChanging) {
                FirebaseCommon.QuantityChanging.INCREASE -> {
                    viewModelScope.launch { _cartProducts.emit(Resources.Loading()) }
                    increaseQuantity(cartProductDocument)
                }
                FirebaseCommon.QuantityChanging.DECREASE -> {
                    if (cartProduct.quantity == 1) {
                        viewModelScope.launch { _deleteDialog.emit(cartProduct) }
                        return
                    }
                    viewModelScope.launch { _cartProducts.emit(Resources.Loading()) }
                    decreaseQuantity(cartProductDocument)
                }
            }
        }
    }

    private fun decreaseQuantity(document: String) {
        firebaseCommon.decreaseQuantity(document) { s, exception ->
            if (exception != null) {
                viewModelScope.launch { _cartProducts.emit(Resources.Error(exception.message.toString())) }
            }
        }
    }

    private fun increaseQuantity(document: String) {
        firebaseCommon.increaseQuantity(document) { s, exception ->
            if (exception != null) {
                viewModelScope.launch { _cartProducts.emit(Resources.Error(exception.message.toString())) }
            }
        }
    }

    fun deleteCartProduct(cartProduct: CartProduct) {
        val index = cartProducts.value.data?.indexOf(cartProduct)
        if (index != null && index != -1) {
            val documentId = cartProductDocuments[index].id
            firestore.collection("users").document(auth.uid!!).collection("cart")
                .document(documentId)
                .delete()
        }
    }


}















