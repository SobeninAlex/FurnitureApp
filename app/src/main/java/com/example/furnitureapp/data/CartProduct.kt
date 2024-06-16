package com.example.furnitureapp.data

data class CartProduct(
    val product: Product,
    val quantity: Int = 1,
    val selectedColor: Int? = null,
    val selectedSize: String? = null
) {

    constructor() : this(
        product = Product(),
        quantity = 1
    )

}