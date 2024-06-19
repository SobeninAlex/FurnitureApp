package com.example.furnitureapp.util

fun Float?.getProductPrice(price: Float): Float {
    if (this == null) {
        return price
    }

    val remainingPricePercentage = 1f - this
    return remainingPricePercentage * price
}