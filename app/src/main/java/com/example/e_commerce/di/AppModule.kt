package com.example.e_commerce.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import com.example.e_commerce.firebase.FirebaseCommon
import com.example.e_commerce.utils.Constants.INTRODUCTION_PREF
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideStorage() = FirebaseStorage.getInstance().reference

    @Provides
    @Singleton
    fun provideFirestore() = FirebaseFirestore.getInstance()

    @Provides
    fun provideSharedPref(application: Application) =
        application.getSharedPreferences(INTRODUCTION_PREF, MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideFirebaseCommon(firestore: FirebaseFirestore, auth: FirebaseAuth) =
        FirebaseCommon(firestore, auth)
}









