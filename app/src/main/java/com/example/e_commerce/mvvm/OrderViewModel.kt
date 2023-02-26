package com.example.e_commerce.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.order.Order
import com.example.e_commerce.data.order.OrderStatus
import com.example.e_commerce.utils.Resources
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OrderViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _order = MutableStateFlow<Resources<Order>>(Resources.Ideal())
    val order = _order.asStateFlow()

    fun placeOrder(order: Order) {
        viewModelScope.launch { _order.emit(Resources.Loading()) }
        firestore.runBatch { batch ->
            //TODO : Add the Order into user orders collection.
            //TODO : Add the Order into orders collection.
            //TODO : Delete the products from user cart collection.

            // 1--
            firestore.collection("users").document(auth.uid!!).collection("orders")
                .document()
                .set(order)

            // 2--
            firestore.collection("orders").document().set(order)

            // 3--
            firestore.collection("users").document(auth.uid!!).collection("cart").get()
                .addOnSuccessListener {
                    it.documents.forEach { documentSnapshot ->
                        documentSnapshot.reference.delete()
                    }
                }
        }.addOnSuccessListener {
            viewModelScope.launch { _order.emit(Resources.Success(order)) }
        }
            .addOnFailureListener {
                viewModelScope.launch { _order.emit(Resources.Error(it.message.toString())) }
            }
    }
}






