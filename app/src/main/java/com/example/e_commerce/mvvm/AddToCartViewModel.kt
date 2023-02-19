package com.example.e_commerce.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.CartProduct
import com.example.e_commerce.firebase.FirebaseCommon
import com.example.e_commerce.utils.Resources
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class AddToCartViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val firebaseCommon: FirebaseCommon
) : ViewModel() {

    private val _addToCart = MutableStateFlow<Resources<CartProduct>>(Resources.Ideal())
    val addToCart = _addToCart.asStateFlow()

    fun addUpdateProductInCart(cartProduct: CartProduct) {
        runBlocking {
            _addToCart.emit(Resources.Loading())
        }
        firestore.collection("users")
            .document(auth.uid!!)
            .collection("cart")
            .whereEqualTo("product.id", cartProduct.product.id)
            .get()
            .addOnSuccessListener {
                it.documents.let {
                    if (it.isEmpty()) { // add new product ..
                        addNewProductToCart(cartProduct)
                    } else {
                        val product = it.first().toObject(CartProduct::class.java)
                        if (product == cartProduct) { // tow product are the same
                            increaseProduct(it.first().id, cartProduct)
                        } else { // add new product
                            addNewProductToCart(cartProduct)
                        }
                    }
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _addToCart.emit(Resources.Error(it.message.toString()))
                }
            }
    }

    private fun addNewProductToCart(cartProduct: CartProduct) {
        firebaseCommon.addNewProduct(cartProduct) { cartProduct1, exception ->
            viewModelScope.launch {
                if (exception == null) {
                    _addToCart.emit(Resources.Success(cartProduct1))
                } else {
                    _addToCart.emit(Resources.Error(exception.message.toString()))
                }
            }
        }
    }

    private fun increaseProduct(docId: String, cartProduct: CartProduct) {
        firebaseCommon.increaseQuantity(docId) { _, exception ->
            viewModelScope.launch {
                if (exception == null)
                    _addToCart.emit(Resources.Success(cartProduct))
                else
                    _addToCart.emit(Resources.Error(exception.message.toString()))
            }
        }
    }
}









