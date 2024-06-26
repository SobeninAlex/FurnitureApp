package com.example.furnitureapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.furnitureapp.data.order.Order
import com.example.furnitureapp.util.Constants.CART_COLLECTION
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
class OrderViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _order = MutableStateFlow<Resource<Order>>(Resource.Initial())
    val order = _order.asStateFlow()

    fun placeOrder(order: Order) {
        _order.value = Resource.Loading()

        firestore.runBatch { batch ->
            //TODO: Add order into user-orders collection
            //TODO: Add order into orders collection
            //TODO: Delete products from user-cart collection

            firestore.collection(USER_COLLECTION)
                .document(auth.uid!!)
                .collection(ORDERS_COLLECTION)
                .document()
                .set(order)

            firestore.collection(ORDERS_COLLECTION)
                .document()
                .set(order)

            firestore.collection(USER_COLLECTION)
                .document(auth.uid!!)
                .collection(CART_COLLECTION)
                .get()
                .addOnSuccessListener {
                    it.documents.forEach { documentSnapshot ->
                        documentSnapshot.reference.delete()
                    }
                }
        }
            .addOnSuccessListener {
                _order.value = Resource.Success(data = order)
            }
            .addOnFailureListener {
                _order.value = Resource.Error(message = it.message.toString())
            }
    }

}