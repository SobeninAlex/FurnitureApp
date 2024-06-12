package com.example.furnitureapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.furnitureapp.data.Product
import com.example.furnitureapp.util.Constants.PRODUCTS_COLLECTION
import com.example.furnitureapp.util.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
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

    private val pagingInfo = PagingInfo()

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

    fun fetchBestProducts() {
        if (!pagingInfo.isPagingEnd) {
            _bestProducts.value = Resource.Loading()
            firestore.collection(PRODUCTS_COLLECTION)
                .orderBy("price", Query.Direction.DESCENDING)
                .limit(pagingInfo.bestProductPage * 4)
                .get()
                .addOnSuccessListener { result ->
                    val bestProductList = result.toObjects(Product::class.java)
                    pagingInfo.isPagingEnd = bestProductList == pagingInfo.oldBestProducts
                    pagingInfo.oldBestProducts = bestProductList
                    _bestProducts.value = Resource.Success(data = bestProductList)
                    pagingInfo.bestProductPage++
                }
                .addOnFailureListener { exception ->
                    _bestProducts.value = Resource.Error(message = exception.message.toString())
                }
        }
    }

    companion object {
        private const val CATEGORY_FIELD = "category"
        private const val SPECIAL_PRODUCTS = "Special Products"
        private const val BEST_DEALS = "Best Deals"
    }

}

internal data class PagingInfo(
    var bestProductPage: Long = 1,
    var oldBestProducts: List<Product> = emptyList(),
    var isPagingEnd: Boolean = false
)

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