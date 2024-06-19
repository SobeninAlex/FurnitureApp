package com.example.furnitureapp.adapters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.furnitureapp.data.CartProduct
import com.example.furnitureapp.databinding.CartProductItemBinding
import com.example.furnitureapp.util.getProductPrice

class CartProductAdapter : ListAdapter<CartProduct, CartProductAdapter.CartProductViewHolder>(DiffCallback) {

    var clickPlus: ((CartProduct) -> Unit)? = null
    var clickMinus: ((CartProduct) -> Unit)? = null
    var onProductClick: ((CartProduct) -> Unit)? = null

    inner class CartProductViewHolder(val binding: CartProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(cartProduct: CartProduct) = with(binding) {
                Glide.with(itemView).load(cartProduct.product.images.first()).into(imgCartProduct)
                tvProductName.text = cartProduct.product.name
                tvProductQuantity.text = cartProduct.quantity.toString()
                val price = cartProduct.product.offerPercentage.getProductPrice(cartProduct.product.price)
                tvProductPrice.text = "$ ${String.format("%.2f", price)}"
                imgProductColor.setImageDrawable(ColorDrawable(cartProduct.selectedColor ?: Color.TRANSPARENT))
                tvProductPrice.text = cartProduct.selectedSize ?: "".also { imgProductSize.setImageDrawable(ColorDrawable(Color.TRANSPARENT)) }
            }
        }

    private object DiffCallback : DiffUtil.ItemCallback<CartProduct>() {
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.product.id == newItem.product.id
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CartProductItemBinding.inflate(inflater, parent, false)
        return CartProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartProductViewHolder, position: Int) {
        val cartProduct = getItem(position)
        holder.bind(cartProduct)

        holder.itemView.setOnClickListener {
            onProductClick?.invoke(cartProduct)
        }

        holder.binding.plusIcon.setOnClickListener {
            clickPlus?.invoke(cartProduct)
        }

        holder.binding.minusIcon.setOnClickListener {
            clickMinus?.invoke(cartProduct)
        }
    }
}