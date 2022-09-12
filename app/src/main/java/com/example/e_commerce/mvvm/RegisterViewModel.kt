package com.example.e_commerce.mvvm

import android.widget.RemoteViewsService
import androidx.lifecycle.ViewModel
import com.example.e_commerce.data.User
import com.example.e_commerce.utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor
    (
    private val firebaseAuth: FirebaseAuth,
    private val database: FirebaseFirestore
) : ViewModel() {

    private val _register = MutableStateFlow<Resources<User>>(Resources.Ideal())
    val register: Flow<Resources<User>> = _register

    private val _registerChannel = Channel<RegisterFailedState>()
    val registerChannel = _registerChannel.receiveAsFlow()

    fun createAccountWithEmailAndPassword(user: User, password: String) {

        if (checkValidation(user, password)) {
            runBlocking {
                _register.emit(Resources.Loading())
            }

            firebaseAuth.createUserWithEmailAndPassword(user.email, password)
                .addOnSuccessListener {
                    it.user.let { firebaseUser ->
                        saveUserToDatabase(firebaseUser!!.uid, user)

                        // _register.value = Resources.Success(firebaseUser)
                    }
                }
                .addOnFailureListener {
                    _register.value = Resources.Error(it.localizedMessage!!)
                }
        } else {
            val registerFailedState = RegisterFailedState(
                validateEmail(user.email),
                validatePassword(password)
            )
            runBlocking {
                _registerChannel.send(registerFailedState)
            }
        }
    }

    private fun saveUserToDatabase(uid: String, user: User) {
        database.collection(Constants.USERS_COLLECTIONS)
            .document(uid)
            .set(user)
            .addOnSuccessListener {
                _register.value = Resources.Success(user)
            }
            .addOnFailureListener {
                _register.value = Resources.Error(it.localizedMessage!!)
            }

    }

    private fun checkValidation(user: User, password: String): Boolean {
        val emailValidate = validateEmail(user.email)
        val passwordValidate = validatePassword(password)

        val shouldRegister = emailValidate is RegisterValidation.Success &&
                passwordValidate is RegisterValidation.Success
        return shouldRegister
    }
}