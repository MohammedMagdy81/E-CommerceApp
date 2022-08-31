package com.example.e_commerce.utils

import android.util.Patterns

fun validateEmail(email:String):RegisterValidation{
    if (email.isEmpty())
        return RegisterValidation.Failed("E-mail Can't be Empty !")

    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        return RegisterValidation.Failed("Wrong E-mail Format !")

    else
        return RegisterValidation.Success
}

fun validatePassword(password:String):RegisterValidation{
    if (password.isEmpty())
        return RegisterValidation.Failed("Password Can't be Empty !")
    if (password.length<6)
        return RegisterValidation.Failed("Password Should be at least 6 Char !")
    else
        return RegisterValidation.Success
}