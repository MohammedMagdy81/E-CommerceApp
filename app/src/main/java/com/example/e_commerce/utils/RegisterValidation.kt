package com.example.e_commerce.utils

// بهندل فيه الريجيجستر كلها يا اما ناجحة او غير ناجحة
sealed class RegisterValidation {
    object Success : RegisterValidation()
    data class Field(val message: String) : RegisterValidation()
}

data class RegisterFieldsState(
    val email: RegisterValidation,
    val password: RegisterValidation
)
