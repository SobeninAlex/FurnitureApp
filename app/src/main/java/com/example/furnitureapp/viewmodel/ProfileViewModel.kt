package com.example.furnitureapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.furnitureapp.data.User
import com.example.furnitureapp.util.Constants.USER_COLLECTION
import com.example.furnitureapp.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val aut: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _user = MutableStateFlow<Resource<User>>(Resource.Initial())
    val user = _user.asStateFlow()

    init {
        getUser()
    }

    private fun getUser() {
        _user.value = Resource.Loading()

//        firestore.collection(USER_COLLECTION)
//            .document(aut.uid!!)
//            .addSnapshotListener { value, error ->
//                if (error != null) {
//                    _user.value = Resource.Error(message = error.message.toString())
//                } else {
//                    val user = value?.toObject(User::class.java)
//                    user?.let {
//                        _user.value = Resource.Success(data = it)
//                    }
//                }
//            }

        firestore.collection(USER_COLLECTION)
            .document(aut.uid!!)
            .get()
            .addOnSuccessListener { result ->
                val user = result.toObject(User::class.java)
                user?.let {
                    _user.value = Resource.Success(data = it)
                }
            }
            .addOnFailureListener {
                _user.value = Resource.Error(message = it.message.toString())
            }
    }

    fun logout() {
        aut.signOut()
    }

}