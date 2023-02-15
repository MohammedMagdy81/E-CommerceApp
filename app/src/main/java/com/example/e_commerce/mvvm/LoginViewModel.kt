package com.example.e_commerce.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.data.User
import com.example.e_commerce.utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _loginState = MutableSharedFlow<Resources<FirebaseUser>>()
    val loginState = _loginState.asSharedFlow()

    private val _loginField = Channel<RegisterFieldsState>()
    val loginField = _loginField.receiveAsFlow()

    private val _resetPassword = MutableSharedFlow<Resources<String>>()
    val resetPassword = _resetPassword.asSharedFlow()

    fun loginUser(email: String, password: String) {

        if (checkValidate(email, password)) {
            runBlocking {
                _loginState.emit(Resources.Loading())
            }
            viewModelScope.launch {
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        viewModelScope.launch {
                            _loginState.emit(Resources.Success(it.user))
                        }
                    }
                    .addOnFailureListener {
                        viewModelScope.launch {
                            _loginState.emit(Resources.Error(it.message.toString()))
                        }
                    }
            }
        } else {
            val loginFieldState =
                RegisterFieldsState(validateEmail(email), validatePassword(password))
            viewModelScope.launch {
                _loginField.send(loginFieldState)
            }
        }
    }

    private fun checkValidate(email: String, password: String): Boolean {
        val emailValidate = validateEmail(email)
        val passwordValidate = validatePassword(password)

        return emailValidate is RegisterValidation.Success &&
                passwordValidate is RegisterValidation.Success
    }

    fun resetPassword(email: String) {
        runBlocking {
            _resetPassword.emit(Resources.Loading())
        }

        firebaseAuth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                viewModelScope.launch {
                    _resetPassword.emit(Resources.Success(email))
                }
            }
            .addOnFailureListener {
                viewModelScope.launch {
                    _resetPassword.emit(Resources.Error(it.message.toString()))
                }
            }

    }


}














