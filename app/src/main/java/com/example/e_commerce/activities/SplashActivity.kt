package com.example.e_commerce.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.e_commerce.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase

class SplashActivity : AppCompatActivity() {

    private var currentUser: FirebaseUser? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentUser = FirebaseAuth.getInstance().currentUser
        setContentView(R.layout.activity_splash)
        if (currentUser == null) {
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this@SplashActivity, LoginRegisterActivity::class.java)
                startActivity(intent)
                finish()
            }, 3000)
        } else {
            val intent = Intent(this@SplashActivity, ShoppingActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}