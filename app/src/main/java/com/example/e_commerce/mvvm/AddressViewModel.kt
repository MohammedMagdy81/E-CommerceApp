package com.example.e_commerce.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.Address
import com.example.e_commerce.utils.Resources
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddressViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _addNewAddress = MutableStateFlow<Resources<Address>>(Resources.Ideal())
    val addNewAddress = _addNewAddress.asStateFlow()

    private val _errorMessage = MutableSharedFlow<String>()
    val errorMessage = _errorMessage.asSharedFlow()

    fun addAddress(address: Address) {
        val validateInputs = validateInputs(address)
        if (validateInputs) {
            viewModelScope.launch { _addNewAddress.emit(Resources.Loading()) }
            firestore.collection("users").document(auth.uid!!).collection("address").document()
                .set(address)
                .addOnSuccessListener {
                    viewModelScope.launch { _addNewAddress.emit(Resources.Success(address)) }
                }
                .addOnFailureListener {
                    viewModelScope.launch { _addNewAddress.emit(Resources.Error(it.message.toString())) }
                }
        } else {
            viewModelScope.launch { _errorMessage.emit("All Field Are Required !") }
        }
    }

    private fun validateInputs(address: Address): Boolean {
        return address.addressTitle.trim().isNotEmpty()
                && address.fullName.trim().isNotEmpty()
                && address.phone.trim().isNotEmpty()
               


    }
}