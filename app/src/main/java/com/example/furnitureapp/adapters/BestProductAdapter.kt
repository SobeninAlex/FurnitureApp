package com.example.furnitureapp.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.furnitureapp.data.Product
import com.example.furnitureapp.databinding.ProductRvItemBinding
import com.example.furnitureapp.util.formattedPrice
import com.example.furnitureapp.util.getProductPrice

class BestProductAdapter : RecyclerView.Adapter<BestProductAdapter.BestProductViewHolder>() {

    var onClick: ((Product) -> Unit)? = null

    inner class BestProductViewHolder(private val binding: ProductRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            with(binding) {
                Glide.with(itemView).load(product.images.first()).into(imgProduct)
                tvName.text = product.name
                val priceAfterOffer = product.offerPercentage.getProductPrice(product.price)
                tvNewPrice.text = priceAfterOffer.formattedPrice()
                tvPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                if (product.offerPercentage == null) {
                    tvNewPrice.visibility = View.INVISIBLE
                }
                tvPrice.text = "$ ${product.price}"
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestProductViewHolder {
        return BestProductViewHolder(
            ProductRvItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: BestProductViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)

        holder.itemView.setOnClickListener {
            onClick?.invoke(product)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

}