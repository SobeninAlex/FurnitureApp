package com.example.furnitureapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.furnitureapp.data.CartProduct
import com.example.furnitureapp.firebase.FirebaseCommon
import com.example.furnitureapp.util.Constants.CART_COLLECTION
import com.example.furnitureapp.util.Constants.USER_COLLECTION
import com.example.furnitureapp.util.Resource
import com.example.furnitureapp.util.getProductPrice
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val firebaseCommon: FirebaseCommon
) : ViewModel(){

    private val _cartProducts = MutableStateFlow<Resource<List<CartProduct>>>(Resource.Initial())
    val cartProducts = _cartProducts.asStateFlow()

    private var cartProductDocuments = emptyList<DocumentSnapshot>()

    val productsPrice = cartProducts.map {
        when (it) {
            is Resource.Success -> {
                calculatePrice(it.data)
            }
            else -> null
        }
    }

    private val _deleteDialog = MutableSharedFlow<CartProduct>()
    val deleteDialog = _deleteDialog.asSharedFlow()

    init {
        getCartProducts()
    }

    private fun calculatePrice(data: List<CartProduct>?): Float {
        return data?.sumByDouble { cartProduct ->
            (cartProduct.product.offerPercentage.getProductPrice(cartProduct.product.price) * cartProduct.quantity).toDouble()
        }?.toFloat() ?: throw RuntimeException("data is null")
    }

    fun deleteCartProduct(cartProduct: CartProduct) {
        val index = cartProducts.value.data?.indexOf(cartProduct)
        if (index != null && index != -1) {
            val documentId = cartProductDocuments[index].id

            firestore.collection(USER_COLLECTION)
                .document(auth.uid!!)
                .collection(CART_COLLECTION)
                .document(documentId)
                .delete()
        }
    }

    private fun getCartProducts() {
        _cartProducts.value = Resource.Loading()
        firestore.collection(USER_COLLECTION)
            .document(auth.uid!!)
            .collection(CART_COLLECTION)
            .addSnapshotListener { value, error ->
                if (error != null || value == null) {
                    _cartProducts.value = Resource.Error(error?.message.toString())
                } else {
                    cartProductDocuments = value.documents
                    val cartProduct = value.toObjects(CartProduct::class.java)
                    _cartProducts.value = Resource.Success(data = cartProduct)
                }
            }
    }

    fun changeQuantity(cartProduct: CartProduct, quantityChanging: FirebaseCommon.QuantityChanging) {
        val index = cartProducts.value.data?.indexOf(cartProduct)

        if (index != null && index != -1) {
            val documentId = cartProductDocuments[index].id
            when (quantityChanging) {
                FirebaseCommon.QuantityChanging.INCREASE -> {
                    _cartProducts.value = Resource.Loading()
                    increaseQuantity(documentId)
                }
                FirebaseCommon.QuantityChanging.DECREASE -> {
                    if (cartProduct.quantity == 1) {
                        viewModelScope.launch {
                            _deleteDialog.emit(cartProduct)
                        }
                        return
                    }
                    _cartProducts.value = Resource.Loading()
                    decreaseQuantity(documentId)
                }
            }
        }
    }

    private fun decreaseQuantity(documentId: String) {
        firebaseCommon.decreaseQuantity(documentId) {res, ex ->
            if (ex != null) {
                _cartProducts.value = Resource.Error(ex.message.toString())
            }
        }
    }

    private fun increaseQuantity(documentId: String) {
        firebaseCommon.increaseQuantity(documentId) {res, ex ->
            if (ex != null) {
                _cartProducts.value = Resource.Error(ex.message.toString())
            }
        }
    }

}