package com.example.furnitureapp.data.order

import com.example.furnitureapp.data.Address
import com.example.furnitureapp.data.CartProduct

data class Order(
    val orderStatus: OrderStatus,
    val totalPrice: Float,
    val products: List<CartProduct>,
    val address: Address
)
