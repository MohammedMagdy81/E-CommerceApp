package com.example.e_commerce.utils

import com.example.e_commerce.data.CartProduct
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class FirebaseCommon @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    val cartCollection = firestore.collection(Constants.USERS_COLLECTIONS)
        .document(auth.uid!!)
        .collection(Constants.CART_COLLECTION)

    fun addProductToCart(cartProduct: CartProduct, onResult: (CartProduct?, Exception?) -> Unit) {
        cartCollection.document().set(cartProduct)
            .addOnSuccessListener {
                onResult(cartProduct, null)
            }.addOnFailureListener {
                onResult(null, it)
            }
    }

    fun increaseQuantity(documentId: String, onResult: (String?, Exception?) -> Unit) {
        firestore.runTransaction { transaction ->
            val docPath = cartCollection.document(documentId)
            val document = transaction.get(docPath)
            val productCartObj = document.toObject(CartProduct::class.java)
            productCartObj?.let { cartProduct ->
                val newQuantity = cartProduct.quantity + 1
                val newProductObj = cartProduct.copy(quantity = newQuantity)
                transaction.set(docPath, newProductObj)
            }
        }.addOnSuccessListener {
            onResult(documentId, null)
        }.addOnFailureListener {
            onResult(null, it)
        }
    }
}













