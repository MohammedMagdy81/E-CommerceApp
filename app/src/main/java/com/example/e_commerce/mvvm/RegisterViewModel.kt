package com.example.e_commerce.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.User
import com.example.e_commerce.utils.*
import com.example.e_commerce.utils.Constants.USER_COLLECTION
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val database: FirebaseFirestore
) : ViewModel() {

    private val _registerState = MutableStateFlow<Resources<User>>(Resources.Ideal())
    val registerState: StateFlow<Resources<User>> = _registerState.asStateFlow()

    private val _validation = Channel<RegisterFieldsState>()
    val validation = _validation.receiveAsFlow()

    fun registerUser(user: User, password: String) {
        if (checkValidation(user, password)) {
            runBlocking {
                _registerState.emit(Resources.Loading())
            }
            firebaseAuth.createUserWithEmailAndPassword(user.email, password)
                .addOnSuccessListener { authResult ->
                    authResult.user?.let { firebaseUser ->
                        viewModelScope.launch {
                            saveUserInfo(firebaseUser.uid, user)
                        }
                    }
                }
                .addOnFailureListener {
                    viewModelScope.launch {
                        _registerState.emit(Resources.Error(it.message))
                    }
                }
        } else {

            val registerFieldState =
                RegisterFieldsState(validateEmail(user.email), validatePassword(password))
            viewModelScope.launch {
                _validation.send(registerFieldState)
            }
        }
    }

    private fun saveUserInfo(uid: String, user: User) {
        database.collection(USER_COLLECTION)
            .document(uid)
            .set(user)
            .addOnSuccessListener {
                viewModelScope.launch {
                    _registerState.emit(Resources.Success(user))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _registerState.emit(Resources.Error(it.message))
                }
            }
    }


    private fun checkValidation(user: User, password: String): Boolean {
        val validateEmail = validateEmail(user.email)
        val validatePassword = validatePassword(password)
        val shouldRegister = validateEmail is RegisterValidation.Success
                && validatePassword is RegisterValidation.Success
        return shouldRegister
    }

}