package com.example.e_commerce.adapters

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce.R
import com.example.e_commerce.data.Address
import com.example.e_commerce.databinding.AddressRvItemBinding

class AddressAdapter : RecyclerView.Adapter<AddressAdapter.AddressViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val itemBinding: AddressRvItemBinding =
            AddressRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddressViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return differList.currentList.size
    }

    var selectedAddress = -1
    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
        val currentAddress = differList.currentList.get(position)
        holder.bind(currentAddress, selectedAddress == position)

        holder.binding.addressBtn.setOnClickListener {
            if (selectedAddress >= 0)
                notifyItemChanged(selectedAddress)
            selectedAddress = holder.adapterPosition
            notifyItemChanged(selectedAddress)
            onItemClick?.invoke(currentAddress)
        }
    }


    private val diffUtil = object : DiffUtil.ItemCallback<Address>() {
        override fun areItemsTheSame(oldItem: Address, newItem: Address): Boolean {
            return newItem.addressTitle == oldItem.addressTitle
        }

        override fun areContentsTheSame(oldItem: Address, newItem: Address): Boolean {
            return oldItem == newItem
        }

    }
    var differList = AsyncListDiffer(this, diffUtil)

    init {
        differList.addListListener { _, _ ->
            notifyItemChanged(selectedAddress)
        }
    }

    inner class AddressViewHolder(val binding: AddressRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(address: Address, isSelected: Boolean) {
            binding.apply {
                addressBtn.text = address.addressTitle
                if (isSelected) {
                    addressBtn.background =
                        ColorDrawable(itemView.context.resources.getColor(R.color.orange_color))
                    addressBtn.setTextColor(itemView.context.resources.getColor(R.color.white))
                } else {
                    addressBtn.background =
                        ColorDrawable(itemView.context.resources.getColor(R.color.g_white))
                    addressBtn.setTextColor(itemView.context.resources.getColor(R.color.g_black))
                }
            }
        }

    }

    var onItemClick: ((Address) -> Unit)? = null
}

