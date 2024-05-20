package com.example.furnitureapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.furnitureapp.data.User
import com.example.furnitureapp.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor (
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _register = MutableStateFlow<Resource<FirebaseUser>>(Resource.Initial())
    val register get() = _register.asStateFlow()

    fun createAccountWithEmailAndPassword(user: User, password: String) {
        _register.value = Resource.Loading()
        firebaseAuth.createUserWithEmailAndPassword(user.email, password)
            .addOnSuccessListener { result ->
                result.user?.let {
                    _register.value = Resource.Success(data = it)
                }
            }
            .addOnFailureListener { error ->
                _register.value = Resource.Error(message = error.message.toString())
            }
    }

}