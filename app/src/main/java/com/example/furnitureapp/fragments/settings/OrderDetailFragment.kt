package com.example.furnitureapp.fragments.settings

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.navArgs
import com.example.furnitureapp.R
import com.example.furnitureapp.adapters.BillingProductAdapter
import com.example.furnitureapp.data.order.Order
import com.example.furnitureapp.data.order.OrderStatus
import com.example.furnitureapp.data.order.getOrderStatus
import com.example.furnitureapp.databinding.FragmentOrderDetailBinding
import com.example.furnitureapp.util.BaseFragment
import com.example.furnitureapp.util.VerticalItemDecoration
import com.example.furnitureapp.util.formattedPrice

class OrderDetailFragment : BaseFragment<FragmentOrderDetailBinding>() {

    private val args by navArgs<OrderDetailFragmentArgs>()
    private val billingAdapter by lazy {
        BillingProductAdapter()
    }

    override fun getLayoutId() = R.layout.fragment_order_detail

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val order = args.order

        setupProductsRv()
        bind(order)
    }

    private fun bind(order: Order) = with(binding) {
        tvOrderId.text = "Order #${order.orderId}"
        tvFullName.text = order.address.fullName
        val address = "${order.address.state}, ${order.address.city}, ${order.address.street}"
        tvAddress.text = address
        tvPhoneNumber.text = order.address.phone
        tvTotalPrice.text = "$ ${order.totalPrice.formattedPrice()}"

        stepView.setSteps(
            mutableListOf(
                OrderStatus.Ordered.status,
                OrderStatus.Confirmed.status,
                OrderStatus.Shipped.status,
                OrderStatus.Delivered.status
            )
        )
        val currentOrderStatus = when(getOrderStatus(order.orderStatus)) {
            OrderStatus.Ordered -> 0
            OrderStatus.Confirmed -> 1
            OrderStatus.Shipped -> 2
            OrderStatus.Delivered -> 3
            else -> 0
        }
        stepView.go(currentOrderStatus, true)
        if (currentOrderStatus == 3) {
            stepView.done(true)
        }

        billingAdapter.submitList(order.products)
    }

    private fun setupProductsRv() {
        binding.rvProducts.apply {
            adapter = billingAdapter
            addItemDecoration(VerticalItemDecoration())
        }
    }
}