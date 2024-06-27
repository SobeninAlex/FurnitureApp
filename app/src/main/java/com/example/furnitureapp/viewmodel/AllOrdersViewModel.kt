package com.example.furnitureapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.furnitureapp.data.order.Order
import com.example.furnitureapp.util.Constants.ORDERS_COLLECTION
import com.example.furnitureapp.util.Constants.USER_COLLECTION
import com.example.furnitureapp.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class AllOrdersViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _allOrders = MutableStateFlow<Resource<List<Order>>>(Resource.Initial())
    val allOrders = _allOrders.asStateFlow()

    init {
        getAllOrders()
    }

    fun getAllOrders() {
        _allOrders.value = Resource.Loading()

        firestore.collection(USER_COLLECTION)
            .document(auth.uid!!)
            .collection(ORDERS_COLLECTION)
            .get()
            .addOnSuccessListener {
                val orders = it.toObjects(Order::class.java)
                _allOrders.value = Resource.Success(data = orders)
            }
            .addOnFailureListener {
                _allOrders.value = Resource.Error(message = it.message.toString())
            }
    }

}