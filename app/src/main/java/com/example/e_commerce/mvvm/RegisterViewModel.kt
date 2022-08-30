package com.example.e_commerce.mvvm

import androidx.lifecycle.ViewModel
import com.example.e_commerce.data.User
import com.example.e_commerce.utils.Resources
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    val firebaseAuth:FirebaseAuth) :ViewModel(){

    private val _register= MutableStateFlow<Resources<FirebaseUser>>(Resources.UnSpecify())
    val register: Flow<Resources<FirebaseUser>> =_register

    fun createUserWithEmailAndPassword(user:User,password:String){
        runBlocking {
            _register.emit(Resources.Loading())
        }
        firebaseAuth.createUserWithEmailAndPassword(user.e_mail,password)
            .addOnSuccessListener {
                it.user.let { firebaseUser->
                    _register.value=Resources.Success(firebaseUser)
                }
            }
            .addOnFailureListener {
                _register.value=Resources.Error(it.localizedMessage)
            }
    }
}