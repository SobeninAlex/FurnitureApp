package com.example.furnitureapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.furnitureapp.data.User
import com.example.furnitureapp.util.Constants.USER_COLLECTION
import com.example.furnitureapp.util.RegisterFieldsState
import com.example.furnitureapp.util.RegisterValidation
import com.example.furnitureapp.util.Resource
import com.example.furnitureapp.util.validateEmail
import com.example.furnitureapp.util.validatePassword
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val db: FirebaseFirestore,
) : ViewModel() {

    private val _register = MutableStateFlow<Resource<User>>(Resource.Initial())
    val register get() = _register.asStateFlow()

    private val _validation = Channel<RegisterFieldsState>()
    val validation = _validation.receiveAsFlow()

    fun createAccountWithEmailAndPassword(user: User, password: String) {
        if (checkValidation(user, password)) {
            _register.value = Resource.Loading()

            firebaseAuth.createUserWithEmailAndPassword(user.email, password)
                .addOnSuccessListener { result ->
                    result.user?.let {
                        saveUserInfo(it.uid, user)
                    }
                }
                .addOnFailureListener { error ->
                    _register.value = Resource.Error(message = error.message.toString())
                }
        } else {
            val registerFieldsState = RegisterFieldsState(
                email = validateEmail(user.email),
                password = validatePassword(password)
            )
            runBlocking {
                _validation.send(registerFieldsState)
            }
        }
    }

    private fun saveUserInfo(userUid: String, user: User) {
        db.collection(USER_COLLECTION)
            .document(userUid)
            .set(user)
            .addOnSuccessListener {
                _register.value = Resource.Success(data = user)
            }
            .addOnFailureListener { error ->
                _register.value = Resource.Error(message = error.message.toString())
            }
    }

    private fun checkValidation(user: User, password: String): Boolean {
        val emailValid = validateEmail(user.email)
        val passwordValid = validatePassword(password)

        return emailValid is RegisterValidation.Success &&
                passwordValid is RegisterValidation.Success
    }

}