package com.example.e_commerce.data

data class User(val firstName:String,
                val lastName:String,
                val e_mail:String,
                val imagePath:String="")
{
    constructor():this("","","","")
}