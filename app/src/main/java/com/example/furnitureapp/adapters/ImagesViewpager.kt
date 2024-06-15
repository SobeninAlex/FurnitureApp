package com.example.furnitureapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.furnitureapp.databinding.ViewpagerImgItemBinding

class ImagesViewpager : RecyclerView.Adapter<ImagesViewpager.ImagesViewpagerViewHolder>() {

    inner class ImagesViewpagerViewHolder(private val binding: ViewpagerImgItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
            fun bind(imgUrl: String) = with(binding) {
                Glide.with(itemView)
                    .load(imgUrl)
                    .into(imgProductDetails)
            }
        }

    private val diffCallback = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesViewpagerViewHolder {
        return ImagesViewpagerViewHolder(
            ViewpagerImgItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ImagesViewpagerViewHolder, position: Int) {
        val imgUrl = differ.currentList[position]
        holder.bind(imgUrl)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
}