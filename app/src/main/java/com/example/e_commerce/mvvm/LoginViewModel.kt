package com.example.e_commerce.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.utils.Resources
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.auth.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _login = MutableSharedFlow<Resources<FirebaseUser>>()
    val login = _login.asSharedFlow()

    private val _resetPassword = MutableSharedFlow<Resources<String>>()
    val resetPassword = _resetPassword.asSharedFlow()

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            _login.emit(Resources.Loading())
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { authResult ->
                viewModelScope.launch {
                    authResult.user.let {
                        _login.emit(Resources.Success(it!!))
                    }
                }

            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _login.emit(Resources.Error(it.localizedMessage!!))
                }
            }
    }

    fun resetPassword(email: String) {
        viewModelScope.launch {
            _resetPassword.emit(Resources.Loading())
        }

        firebaseAuth.sendPasswordResetEmail(email)
            .addOnCompleteListener{
                viewModelScope.launch {
                    _resetPassword.emit(Resources.Success(email))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _resetPassword.emit(Resources.Error(it.localizedMessage!!))
                }
            }
    }
}
