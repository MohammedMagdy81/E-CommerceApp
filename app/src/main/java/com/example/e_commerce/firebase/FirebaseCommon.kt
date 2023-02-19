package com.example.e_commerce.firebase

import com.example.e_commerce.data.CartProduct
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseCommon(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {

    private val cartCollection = firestore.collection("users").document(auth.uid!!)
        .collection("cart")

    fun addNewProduct(cartProduct: CartProduct, onResult: (CartProduct?, Exception?) -> Unit) {
        cartCollection.document()
            .set(cartProduct)
            .addOnSuccessListener {
                onResult(cartProduct, null)
            }
            .addOnFailureListener {
                onResult(null, it)
            }
    }

    fun increaseQuantity(documentId: String, onResult: (String?, Exception?) -> Unit) {
        firestore.runTransaction { transaction ->
            val documentRef = cartCollection.document(documentId)
            val document = transaction.get(documentRef)
            val productObj = document.toObject(CartProduct::class.java)

            productObj?.let { cartProduct ->
                val newQuantity = cartProduct.quantity + 1
                val newCartObj = cartProduct.copy(quantity = newQuantity)
                transaction.set(documentRef, newCartObj)
            }
        }
            .addOnSuccessListener { onResult(documentId, null) }
            .addOnFailureListener { onResult(null, it) }
    }


}