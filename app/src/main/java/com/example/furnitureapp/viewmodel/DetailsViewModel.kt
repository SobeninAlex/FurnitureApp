package com.example.furnitureapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.furnitureapp.data.CartProduct
import com.example.furnitureapp.firebase.FirebaseCommon
import com.example.furnitureapp.util.Constants.CART_COLLECTION
import com.example.furnitureapp.util.Constants.ID_FIELD
import com.example.furnitureapp.util.Constants.PRODUCT_FIELD
import com.example.furnitureapp.util.Constants.USER_COLLECTION
import com.example.furnitureapp.util.Resource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val firestopre: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val firebaseCommon: FirebaseCommon
) : ViewModel() {

    private val _addToCart = MutableStateFlow<Resource<CartProduct>>(Resource.Initial())
    val addToCart = _addToCart.asStateFlow()

    fun addUpdateProductInCart(cartProduct: CartProduct) {
        _addToCart.value = Resource.Loading()

        firestopre.collection(USER_COLLECTION)
            .document(auth.uid!!)
            .collection(CART_COLLECTION)
            .whereEqualTo("$PRODUCT_FIELD.$ID_FIELD", cartProduct.product.id)
            .get()
            .addOnSuccessListener { result ->
                result.documents.let {
                    if (it.isEmpty()) {
                        addNewProduct(cartProduct)
                    } else {
                        val product = it.first().toObject(CartProduct::class.java)
                        if (product == cartProduct) {
                            val documentId = it.first().id
                            increaseQuantity(documentId, cartProduct)
                        } else {
                            addNewProduct(cartProduct)
                        }
                    }
                }
            }
            .addOnFailureListener { exception ->
                _addToCart.value = Resource.Error(message = exception.message.toString())
            }
    }

    private fun addNewProduct(cartProduct: CartProduct) {
        firebaseCommon.addProductToCart(cartProduct) { addedProduct, ex ->
            if (ex == null) {
                _addToCart.value = Resource.Success(data = addedProduct!!)
            } else {
                _addToCart.value = Resource.Error(message = ex.message.toString())
            }
        }
    }

    private fun increaseQuantity(documentId: String, cartProduct: CartProduct) {
        firebaseCommon.increaseQuantity(documentId) { _, ex ->
            if (ex == null) {
                _addToCart.value = Resource.Success(data = cartProduct)
            } else {
                _addToCart.value = Resource.Error(message = ex.message.toString())
            }
        }
    }

}