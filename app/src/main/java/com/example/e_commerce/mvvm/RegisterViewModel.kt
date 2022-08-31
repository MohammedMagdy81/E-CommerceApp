package com.example.e_commerce.mvvm

import androidx.lifecycle.ViewModel
import com.example.e_commerce.data.User
import com.example.e_commerce.utils.RegisterValidation
import com.example.e_commerce.utils.Resources
import com.example.e_commerce.utils.validateEmail
import com.example.e_commerce.utils.validatePassword
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    val firebaseAuth:FirebaseAuth) :
    ViewModel(){

    private val _register= MutableStateFlow<Resources<FirebaseUser>>(Resources.UnSpecify())
    val register: Flow<Resources<FirebaseUser>> =_register

    private val _validation= Channel<RegisterValidation.RegisterFailedState>()
    val validation= _validation.receiveAsFlow()

    fun createUserWithEmailAndPassword(user:User,password:String){
       if (checkValidation(user,password)) {

           runBlocking {
               _register.emit(Resources.Loading())
           }
           firebaseAuth.createUserWithEmailAndPassword(user.e_mail, password)
               .addOnSuccessListener {
                   it.user.let { firebaseUser ->
                       _register.value = Resources.Success(firebaseUser)
                   }
               }
               .addOnFailureListener {
                   _register.value = Resources.Error(it.localizedMessage)
               }

       }else{
           val registerFailedState= RegisterValidation.RegisterFailedState(
               validateEmail(user.e_mail),
               validatePassword(password)
           )

           runBlocking {
               _validation.send(registerFailedState)
           }
       }
    }

    private fun checkValidation(user: User, password: String) :Boolean{
        val emailValidate= validateEmail(user.e_mail)
        val passwordValidate= validatePassword(password)
        val shouldRegister= emailValidate is RegisterValidation.Success &&
                passwordValidate is RegisterValidation.Success

        return shouldRegister
    }
}