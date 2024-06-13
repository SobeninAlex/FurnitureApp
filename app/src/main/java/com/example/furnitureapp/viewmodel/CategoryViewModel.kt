package com.example.furnitureapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.furnitureapp.data.Category
import com.example.furnitureapp.data.Product
import com.example.furnitureapp.util.Constants.CATEGORY_FIELD
import com.example.furnitureapp.util.Constants.OFFER_PERCENTAGE_FIELD
import com.example.furnitureapp.util.Constants.PRODUCTS_COLLECTION
import com.example.furnitureapp.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CategoryViewModel constructor(
    private val firestore: FirebaseFirestore,
    private val category: Category
) : ViewModel() {

    private val _offerProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Initial())
    val offerProducts = _offerProducts.asStateFlow()

    private val _bestProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Initial())
    val bestProducts = _bestProducts.asStateFlow()

    init {
        fetchOfferProducts()
        fetchBestProducts()
    }

    fun fetchOfferProducts() {
        _offerProducts.value = Resource.Loading()
        firestore.collection(PRODUCTS_COLLECTION)
            .whereEqualTo(CATEGORY_FIELD, category.category)
            .whereNotEqualTo(OFFER_PERCENTAGE_FIELD, null)
            .get()
            .addOnSuccessListener { result ->
                val products = result.toObjects(Product::class.java)
                _offerProducts.value = Resource.Success(data = products)
            }
            .addOnFailureListener { exception ->
                _offerProducts.value = Resource.Error(exception.message.toString())
                Log.d("CategoryViewModel", exception.message.toString())
            }
    }

    fun fetchBestProducts() {
        _bestProducts.value = Resource.Loading()
        firestore.collection(PRODUCTS_COLLECTION)
            .whereEqualTo(CATEGORY_FIELD, category.category)
            .whereEqualTo(OFFER_PERCENTAGE_FIELD, null)
            .get()
            .addOnSuccessListener { result ->
                val products = result.toObjects(Product::class.java)
                _bestProducts.value = Resource.Success(data = products)
            }
            .addOnFailureListener { exception ->
                _bestProducts.value = Resource.Error(exception.message.toString())
                Log.d("CategoryViewModel", exception.message.toString())
            }
    }

}