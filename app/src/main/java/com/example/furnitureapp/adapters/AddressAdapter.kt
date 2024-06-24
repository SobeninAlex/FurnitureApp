package com.example.furnitureapp.adapters

import android.content.res.Resources
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.furnitureapp.R
import com.example.furnitureapp.data.Address
import com.example.furnitureapp.databinding.AddressRvItemBinding

class AddressAdapter(
    private val listener: AddressAdapterListener
) : ListAdapter<Address, AddressAdapter.AddressViewHolder>(DifferCallback) {

    interface AddressAdapterListener {
        fun onAddressClick(address: Address)
    }

    private var selectedAddress = -1

    inner class AddressViewHolder(val binding: AddressRvItemBinding) : ViewHolder(binding.root) {
        fun bind(address: Address, isSelected: Boolean) {
            with(binding) {
                buttonAddress.text = address.addressTitle
                val colorButton = if (isSelected) {
                    R.color.g_blue
                } else {
                    R.color.g_white
                }
                buttonAddress.setBackgroundResource(colorButton)
            }
        }
    }

    private object DifferCallback : DiffUtil.ItemCallback<Address>() {
        override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem.addressTitle == newItem.addressTitle && oldItem.fullName == newItem.fullName
        }

        override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        return AddressViewHolder(
            AddressRvItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val address = getItem(position)
        holder.bind(address, selectedAddress == position)

        holder.binding.buttonAddress.setOnClickListener {
            if (selectedAddress >= 0) {
                notifyItemChanged(selectedAddress)
            }
            selectedAddress = holder.adapterPosition
            notifyItemChanged(selectedAddress)
            listener.onAddressClick(address)
        }
    }

}