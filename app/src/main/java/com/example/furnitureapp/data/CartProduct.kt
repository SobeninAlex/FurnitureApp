package com.example.furnitureapp.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartProduct(
    val product: Product,
    val quantity: Int = 1,
    val selectedColor: Int? = null,
    val selectedSize: String? = null
) : Parcelable {

    constructor() : this(
        product = Product(),
        quantity = 1
    )

}