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

    private val _bestDealsProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Initial())
    val bestDealsProducts = _bestDealsProducts.asStateFlow()

    private val _bestProducts = MutableStateFlow<Resource<List<Product>>>(Resource.Initial())
    val bestProducts = _bestProducts.asStateFlow()

    init {
        fetchSpecialProducts()
        fetchBestDealsProducts()
        fetchBestProducts()
    }

    private fun fetchSpecialProducts() {
        _specialProducts.value = Resource.Loading()
        firestore.collection(PRODUCTS_COLLECTION)
            .whereEqualTo(CATEGORY_FIELD, SPECIAL_PRODUCTS)
            .get()
            .addOnSuccessListener { result ->
                val specialProductList = result.toObjects(Product::class.java)
                _specialProducts.value = Resource.Success(data = specialProductList)
            }
            .addOnFailureListener { exception ->
                _specialProducts.value = Resource.Error(message = exception.message.toString())
            }
    }

    private fun fetchBestDealsProducts() {
        _bestDealsProducts.value = Resource.Loading()
        firestore.collection(PRODUCTS_COLLECTION)
            .whereEqualTo(CATEGORY_FIELD, BEST_DEALS)
            .get()
            .addOnSuccessListener { result ->
                val bestDealsProductList = result.toObjects(Product::class.java)
                _bestDealsProducts.value = Resource.Success(data = bestDealsProductList)
            }
            .addOnFailureListener { exception ->
                _bestDealsProducts.value = Resource.Error(message = exception.message.toString())
            }
    }

    private fun fetchBestProducts() {
        _bestProducts.value = Resource.Loading()
        firestore.collection(PRODUCTS_COLLECTION)
            .get()
            .addOnSuccessListener { result ->
                val bestProductList = result.toObjects(Product::class.java)
                _bestProducts.value = Resource.Success(data = bestProductList)
            }
            .addOnFailureListener { exception ->
                _bestProducts.value = Resource.Error(message = exception.message.toString())
            }
    }

    companion object {
        private const val CATEGORY_FIELD = "category"
        private const val SPECIAL_PRODUCTS = "Special Products"
        private const val BEST_DEALS = "Best Deals"
    }

}

/**
 * <string-array name="categories">
 *         <item>Special Products</item>
 *         <item>Best Deals</item>
 *         <item>Chair</item>
 *         <item>Cupboard</item>
 *         <item>Table</item>
 *         <item>Accessory</item>
 *         <item>Furniture</item>
 *     </string-array>
 */