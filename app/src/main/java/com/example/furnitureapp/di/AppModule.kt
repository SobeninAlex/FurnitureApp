package com.example.furnitureapp.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import com.example.furnitureapp.util.Constants.INTRODUCTION_SP
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
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
    fun provideFirebaseFirestoreDatabase() = Firebase.firestore

    @Provides
    @Singleton
    fun provideStorageReference() = Firebase.storage.reference

    @Provides
    fun provideIntroductionSharedPreferences(application: Application) =
        application.getSharedPreferences(INTRODUCTION_SP, MODE_PRIVATE)

}