package com.example.furnitureapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.furnitureapp.databinding.SizeRvItemBinding

class SizesAdapter : ListAdapter<String, SizesAdapter.SizesViewHolder>(DiffCallback) {

    private var selectedPosition = -1

    var onItemClick: ((String) -> Unit)? = null

    inner class SizesViewHolder(private val binding: SizeRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String, position: Int) = with(binding) {
            tvSize.text = item
            if (position == selectedPosition) {
                imageShadow.visibility = View.VISIBLE
                imagePicked.visibility = View.INVISIBLE
            } else {
                imageShadow.visibility = View.INVISIBLE
                imagePicked.visibility = View.INVISIBLE
            }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizesViewHolder {
        val infalter = LayoutInflater.from(parent.context)
        val binding = SizeRvItemBinding.inflate(infalter, parent, false)
        return SizesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SizesViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, position)

        holder.itemView.setOnClickListener {
            if (selectedPosition >= 0) {
                notifyItemChanged(selectedPosition)
            }
            selectedPosition = holder.adapterPosition
            notifyItemChanged(selectedPosition)
            onItemClick?.invoke(item)
        }
    }
}