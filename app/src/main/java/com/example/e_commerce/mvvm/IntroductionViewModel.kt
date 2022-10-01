package com.example.e_commerce.mvvm

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.R
import com.example.e_commerce.utils.Constants.IS_BUTTON_CLICK_KEY
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroductionViewModel @Inject constructor(
    private val sharedPref:SharedPreferences,
    private val firebaseAuth: FirebaseAuth
):ViewModel() {

    private val _navigateState= MutableStateFlow(0)
    val navigateState: StateFlow<Int> = _navigateState

    companion object{
        const val SHOPPING_ACTIVITY=12
        const val ACCOUNT_OPTION_FRAGMENT= 13
    }

    init {
        val isButtonClicked=sharedPref.getBoolean(IS_BUTTON_CLICK_KEY,false)
        val user= firebaseAuth.currentUser

        if (user !=null){
            // navigate to shopping Fragment
            viewModelScope.launch {
                _navigateState.emit(SHOPPING_ACTIVITY)
            }

        }else if (isButtonClicked){
            // navigate to account option ..
            viewModelScope.launch {
                _navigateState.emit(ACCOUNT_OPTION_FRAGMENT)
            }
        }else{
            Unit
        }
    }



    fun setUpButtonClick(){
        sharedPref.edit().putBoolean(IS_BUTTON_CLICK_KEY,true).apply()
    }





}