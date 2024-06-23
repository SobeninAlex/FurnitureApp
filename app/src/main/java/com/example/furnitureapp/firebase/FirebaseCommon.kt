package com.example.furnitureapp.firebase

import com.example.furnitureapp.data.CartProduct
import com.example.furnitureapp.util.Constants.CART_COLLECTION
import com.example.furnitureapp.util.Constants.USER_COLLECTION
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class FirebaseCommon @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: FirebaseAuth
) {

    private val cartCollection = firestore.collection(USER_COLLECTION)
        .document(auth.uid!!)
        .collection(CART_COLLECTION)

    fun addProductToCart(cartProduct: CartProduct, onResult: (CartProduct?, Exception?) -> Unit) {
        cartCollection.document()
            .set(cartProduct)
            .addOnSuccessListener {
                onResult(cartProduct, null)
            }
            .addOnFailureListener {
                onResult(null, it)
            }
    }

    fun increaseQuantity(documentId: String, onResult: (String?, Exception?) -> Unit) {
        firestore
            .runTransaction { transaction ->
                val documentRef = cartCollection.document(documentId)
                val document = transaction.get(documentRef)
                val productObject = document.toObject(CartProduct::class.java)
                productObject?.let { cartProduct ->
                    val newQuantity = cartProduct.quantity + 1
                    val newProductObject = cartProduct.copy(quantity = newQuantity)
                    transaction.set(documentRef, newProductObject)
                }
            }
            .addOnSuccessListener {
                onResult(documentId, null)
            }
            .addOnFailureListener {
                onResult(null, it)
            }
    }

    fun decreaseQuantity(documentId: String, onResult: (String?, Exception?) -> Unit) {
        firestore
            .runTransaction { transaction ->
                val documentRef = cartCollection.document(documentId)
                val document = transaction.get(documentRef)
                val productObject = document.toObject(CartProduct::class.java)
                productObject?.let { cartProduct ->
                    val newQuantity = cartProduct.quantity - 1
                    val newProductObject = cartProduct.copy(quantity = newQuantity)
                    transaction.set(documentRef, newProductObject)
                }
            }
            .addOnSuccessListener {
                onResult(documentId, null)
            }
            .addOnFailureListener {
                onResult(null, it)
            }
    }

    enum class QuantityChanging {
        INCREASE, DECREASE
    }

}