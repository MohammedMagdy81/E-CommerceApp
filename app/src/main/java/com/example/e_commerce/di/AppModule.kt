package com.example.e_commerce.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module // this to tell dagger hilt that is a module
@InstallIn(SingletonComponent::class) // /// scope if this module
object AppModule {

    @Provides // that's mean is that is new dependency
    @Singleton // one instance in app
    fun provideFirebaseAuth()= FirebaseAuth.getInstance()

}