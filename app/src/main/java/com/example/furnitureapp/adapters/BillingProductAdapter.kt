package com.example.furnitureapp.adapters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.furnitureapp.data.CartProduct
import com.example.furnitureapp.databinding.BillingProductsRvItemBinding
import com.example.furnitureapp.util.formattedPrice
import com.example.furnitureapp.util.getProductPrice

class BillingProductAdapter() : ListAdapter<CartProduct, BillingProductAdapter.BillingProductViewHolder>(DifferCallback) {

    inner class BillingProductViewHolder(val binding: BillingProductsRvItemBinding) :
        ViewHolder(binding.root) {
        fun bind(cartProduct: CartProduct) {
            with(binding) {
                Glide.with(itemView).load(cartProduct.product.images.first()).into(imageCartProduct)
                tvProductCartName.text = cartProduct.product.name
                tvBillingProductQuantity.text = cartProduct.quantity.toString()
                val priceAfterOffer =
                    cartProduct.product.offerPercentage.getProductPrice(cartProduct.product.price)
                tvProductCartPrice.text = priceAfterOffer.formattedPrice()
                imageCartProductColor.setImageDrawable(
                    ColorDrawable(
                        cartProduct.selectedColor ?: Color.TRANSPARENT
                    )
                )
                tvCartProductSize.text = cartProduct.selectedSize ?: "".also {
                    imageCartProductSize.setImageDrawable(
                        ColorDrawable(Color.TRANSPARENT)
                    )
                }
            }
        }
    }

    private object DifferCallback : DiffUtil.ItemCallback<CartProduct>() {
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.product == newItem.product
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillingProductViewHolder {
        return BillingProductViewHolder(
            BillingProductsRvItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BillingProductViewHolder, position: Int) {
        val cartProduct = getItem(position)
        holder.bind(cartProduct)
    }

}