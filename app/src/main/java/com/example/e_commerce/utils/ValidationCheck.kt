package com.example.e_commerce.utils

import android.util.Patterns


// بهندل فيها نجاح الايميل بس
fun validateEmail(email: String): RegisterValidation {
    if (email.isEmpty())
        return RegisterValidation.Field("الايميل لا يمكن أن يكون فارغا!")
    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        return RegisterValidation.Field("صيغة الايميل غير صحيحة !")
    return RegisterValidation.Success
}

// بهندل فيها نجاح الباسوورد بس
fun validatePassword(password: String): RegisterValidation {
    if (password.isEmpty())
        return RegisterValidation.Field("كلمة السر لا يمكن أن تكون فارغة !")
    if (password.length < 6)
        return RegisterValidation.Field("كلمة السر لا يمكن أن تكون أقل من 6 حروف !")
    return RegisterValidation.Success
}