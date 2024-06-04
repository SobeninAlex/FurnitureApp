package com.example.furnitureapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.furnitureapp.data.Product
import com.example.furnitureapp.util.Constants.PRODUCTS_COLLECTION
import com.example.furnitureapp.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class MainCategoryViewModel @Inject constructor(
    private val firestore: FirebaseFirestore
) : ViewModel() {

    private val _specialProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Initial())
    val specialProducts = _specialProducts.asStateFlow()

    init {
        fetchSpecialProducts()
    }

    private fun fetchSpecialProducts() {
        _specialProducts.value = Resource.Loading()
        firestore.collection(PRODUCTS_COLLECTION)
            .whereEqualTo("category", "Special Products")
            .get()
            .addOnSuccessListener { result ->
                val specialProductList = result.toObjects(Product::class.java)
                _specialProducts.value = Resource.Success(data = specialProductList)
            }
            .addOnFailureListener { exception ->
                _specialProducts.value = Resource.Error(message = exception.message.toString())
            }
    }

}