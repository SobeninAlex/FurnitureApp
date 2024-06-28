package com.example.furnitureapp.adapters

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.furnitureapp.R
import com.example.furnitureapp.data.order.Order
import com.example.furnitureapp.data.order.OrderStatus
import com.example.furnitureapp.data.order.getOrderStatus
import com.example.furnitureapp.databinding.OrderItemBinding

class AllOrdersAdapter(
    private val listener: OrdersAdapterListener
) : ListAdapter<Order, AllOrdersAdapter.OrdersViewHolder>(DifferCallback) {

    interface OrdersAdapterListener {
        fun onOrderClick(order: Order)
    }

    inner class OrdersViewHolder(val binding: OrderItemBinding) : ViewHolder(binding.root) {
        fun bind(order: Order) = with(binding) {
            tvOrderId.text = order.orderId.toString()
            tvOrderDate.text = order.date

            with(itemView.context) {
                val colorDrawable = when (getOrderStatus(order.orderStatus)) {
                    is OrderStatus.Canceled -> {
                        ColorDrawable(getColor(R.color.g_red))
                    }
                    is OrderStatus.Confirmed -> {
                        ColorDrawable(getColor(R.color.g_green))
                    }
                    is OrderStatus.Delivered -> {
                        ColorDrawable(getColor(R.color.g_green))
                    }
                    is OrderStatus.Ordered -> {
                        ColorDrawable(getColor(R.color.g_orange_yellow))
                    }
                    is OrderStatus.Returned -> {
                        ColorDrawable(getColor(R.color.g_red))
                    }
                    is OrderStatus.Shipped -> {
                        ColorDrawable(getColor(R.color.g_green))
                    }
                }

                imageOrderState.setImageDrawable(colorDrawable)
            }
        }
    }

    private object DifferCallback : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.orderId == newItem.orderId
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        return OrdersViewHolder(
            OrderItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        val order = getItem(position)
        holder.bind(order)

        holder.itemView.setOnClickListener {
            listener.onOrderClick(order)
        }
    }

}