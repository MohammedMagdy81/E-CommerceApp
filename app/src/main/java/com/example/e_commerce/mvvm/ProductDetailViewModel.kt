package com.example.e_commerce.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.CartProduct
import com.example.e_commerce.utils.Constants
import com.example.e_commerce.utils.FirebaseCommon
import com.example.e_commerce.utils.Resources
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val firebaseCommon: FirebaseCommon
) : ViewModel() {
    private val _addCart = MutableStateFlow<Resources<CartProduct>>(Resources.Ideal())
    val addCart: StateFlow<Resources<CartProduct>> = _addCart.asStateFlow()

    fun addUpdateProductCart(cart: CartProduct) {

        viewModelScope.launch {
            _addCart.emit(Resources.Loading())
        }
        firestore.collection(Constants.USERS_COLLECTIONS)
            .document(auth.currentUser!!.uid)
            .collection(Constants.CART_COLLECTION)
            .whereEqualTo("product.id", cart.product.id)
            .get()
            .addOnSuccessListener {
                it.documents?.let {
                    if (it.isEmpty()) { // add new product
                        addProductToCart(cart)
                    } else {
                        val product = it.first().toObject(CartProduct::class.java)
                        if (product == cart) {// increase Quantity
                            val docId = it.first().id
                            increaseQuantity(docId, cart)
                        } else {// add new product
                            addProductToCart(cart)
                        }
                    }
                }
            }.addOnFailureListener {
                viewModelScope.launch {
                    _addCart.emit(Resources.Error(it.message.toString()))
                }
            }

    }

    private fun addProductToCart(produtc: CartProduct) {
        firebaseCommon.addProductToCart(produtc) { cartProduct, exception ->
            viewModelScope.launch {
                if (exception == null) {
                    _addCart.emit(Resources.Success(cartProduct!!))
                } else {
                    _addCart.emit(Resources.Error(exception.message.toString()))
                }
            }
        }
    }

    private fun increaseQuantity(docId: String, cartProduct: CartProduct) {
        firebaseCommon.increaseQuantity(docId) { _, e ->
            viewModelScope.launch {
                if (e == null) {
                    _addCart.emit(Resources.Success(cartProduct))
                } else {
                    _addCart.emit(Resources.Error(e.message.toString()))
                }
            }
        }
    }
}






