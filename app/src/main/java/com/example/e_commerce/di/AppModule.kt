package com.example.e_commerce.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.e_commerce.utils.Constants.INTRODUCTION_SHARED_PREF_NAME
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module // this to tell dagger hilt that is a module
@InstallIn(SingletonComponent::class) // /// scope if this module
object AppModule {

    @Provides // that's mean is that is new dependency
    @Singleton // one instance in app
    fun provideFirebaseAuth()= FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseFireStore()=FirebaseFirestore.getInstance()

    @Provides
    fun provideIntroductionSharedPref(@ApplicationContext app:Context)
    =app.getSharedPreferences(INTRODUCTION_SHARED_PREF_NAME,MODE_PRIVATE)
}