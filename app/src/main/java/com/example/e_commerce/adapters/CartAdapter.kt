package com.example.e_commerce.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce.data.Product
import com.example.e_commerce.databinding.CartProductItemBinding

class CartAdapter : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    val diffUtil = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }


        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }
    var differList = AsyncListDiffer(this, diffUtil)

    inner class CartViewHolder(val binding: CartProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product?) {
            product?.let {


            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding: CartProductItemBinding =
            CartProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val product = differList.currentList.get(position)
        holder.bind(product)
    }

    override fun getItemCount(): Int {
        return differList.currentList.size
    }

}