package com.example.e_commerce.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_commerce.data.Product
import com.example.e_commerce.databinding.LayoutSpecialProductItemBinding
import com.example.e_commerce.utils.getProductPrice

class SpecialProductsAdapter :
    RecyclerView.Adapter<SpecialProductsAdapter.SpecialProductsViewHolder>() {

    inner class SpecialProductsViewHolder(val binding: LayoutSpecialProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.apply {
                Glide.with(itemView).load(product.images[0]).centerInside().into(specialProductImage)
                specialProductName.text = product.name

                val priceAfterDiscount = product.offerPercentage.getProductPrice(product.price)
                specialPRoductPrice.text = "$ ${String.format("%.2f", priceAfterDiscount)}"
                //specialPRoductPrice.text = product.price.toString()
            }
        }
    }

    val diffUtil = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialProductsViewHolder {
        val itemBinding: LayoutSpecialProductItemBinding =
            LayoutSpecialProductItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return SpecialProductsViewHolder(itemBinding)
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: SpecialProductsViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
        holder.binding.btnAddToCart.setOnClickListener {
            onItemClick?.invoke(differ.currentList[position])
        }
    }

    var onItemClick: ((Product) -> Unit)? = null
}