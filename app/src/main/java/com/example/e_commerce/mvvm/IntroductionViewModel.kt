package com.example.e_commerce.mvvm

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.e_commerce.utils.Constants.INTRODUCTION_KEY
import com.example.e_commerce.utils.Constants.NAVIGATE_TO_ACCOUNT_OPTION
import com.example.e_commerce.utils.Constants.NAVIGATE_TO_SHOPPING
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroductionViewModel @Inject constructor(
    private val sharedPref: SharedPreferences,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _navigateState = MutableStateFlow(0)
    val navigateState: StateFlow<Int> = _navigateState


    init {
        val isButtonClicked = sharedPref.getBoolean(INTRODUCTION_KEY, false)
        val user = firebaseAuth.currentUser

        if (user != null) {
            // navigate to shopping Fragment
            viewModelScope.launch {
                _navigateState.emit(NAVIGATE_TO_SHOPPING)
            }

        } else if (isButtonClicked) {
            // navigate to account option ..
            viewModelScope.launch {
                _navigateState.emit(NAVIGATE_TO_ACCOUNT_OPTION)
            }
        } else {
            Unit
        }
    }


    fun makeButtonClick() {
        sharedPref.edit().putBoolean(INTRODUCTION_KEY,true).apply()
    }
}