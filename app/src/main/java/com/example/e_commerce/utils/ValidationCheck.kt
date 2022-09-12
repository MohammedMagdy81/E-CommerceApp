package com.example.e_commerce.utils

import android.util.Patterns

fun validateEmail(email: String): RegisterValidation {
    if (email.isEmpty())
        return RegisterValidation.Failed("Email Can't be Empty !")
    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        return RegisterValidation.Failed("Wrong Email Format !")
    return RegisterValidation.Success

}

fun validatePassword(password: String): RegisterValidation {
    if (password.isEmpty() || password.length < 6)
        return RegisterValidation.Failed("Password Should be 6 Char At least !")
    return RegisterValidation.Success

}