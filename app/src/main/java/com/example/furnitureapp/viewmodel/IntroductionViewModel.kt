package com.example.furnitureapp.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.furnitureapp.R
import com.example.furnitureapp.util.Constants.INTRODUCTION_KEY
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class IntroductionViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _navigate = MutableStateFlow(0)
    val navigate = _navigate.asStateFlow()

    companion object {
        const val SHOPPING_ACTIVITY = 23
        val ACCOUNT_OPTIONS_FRAGMENT = R.id.action_introductionFragment_to_accountOptionsFragment
    }

    init {
        val isButtonClicked = sharedPreferences.getBoolean(INTRODUCTION_KEY, false)
        val user = firebaseAuth.currentUser

        if (user != null) {
            viewModelScope.launch {
                _navigate.emit(SHOPPING_ACTIVITY)
            }
        } else if (isButtonClicked) {
            viewModelScope.launch {
                _navigate.emit(ACCOUNT_OPTIONS_FRAGMENT)
            }
        } else Unit
    }

    fun startButtonClick() {
        sharedPreferences.edit()
            .putBoolean(INTRODUCTION_KEY, true)
            .apply()
    }

}