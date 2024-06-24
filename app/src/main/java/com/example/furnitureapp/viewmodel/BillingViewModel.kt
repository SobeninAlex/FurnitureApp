package com.example.furnitureapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.furnitureapp.data.Address
import com.example.furnitureapp.util.Constants.ADDRESS_COLLECTION
import com.example.furnitureapp.util.Constants.USER_COLLECTION
import com.example.furnitureapp.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class BillingViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _address = MutableStateFlow<Resource<List<Address>>>(Resource.Initial())
    val address = _address.asStateFlow()

    init {
        getUserAddresses()
    }

    fun getUserAddresses() {
        _address.value = Resource.Loading()

        firestore.collection(USER_COLLECTION)
            .document(auth.uid!!)
            .collection(ADDRESS_COLLECTION)
            .get()
            .addOnSuccessListener { result ->
                val addresses = result.toObjects(Address::class.java)
                _address.value = Resource.Success(data = addresses)
            }
            .addOnFailureListener {
                _address.value = Resource.Error(message = it.message.toString())
            }

//        firestore.collection(USER_COLLECTION)
//            .document(auth.uid!!)
//            .collection(ADDRESS_COLLECTION)
//            .addSnapshotListener { value, error ->
//                if (error != null) {
//                    _address.value = Resource.Error(message = error.message.toString())
//                    return@addSnapshotListener
//                }
//
//                val addresses = value?.toObjects(Address::class.java)
//                addresses?.let { _address.value = Resource.Success(data = it) }
//            }

    }

}