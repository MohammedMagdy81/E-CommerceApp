package com.example.e_commerce.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_commerce.data.Product
import com.example.e_commerce.databinding.LayoutProductItemBinding
import com.example.e_commerce.utils.getProductPrice
import java.util.*

class BestProductsAdapter : RecyclerView.Adapter<BestProductsAdapter.BestProductsViewHolder>() {

    inner class BestProductsViewHolder(val binding: LayoutProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.apply {
                Glide.with(itemView).load(product.images.first()).into(imgProduct)
                tvName.text = product.name
                val priceAfterDiscount = product.offerPercentage.getProductPrice(product.price)
                tvNewPrice.text = "$ ${String.format(Locale.getDefault(),"%.2f", priceAfterDiscount)}"
                if (product.offerPercentage!=null){
                    tvPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                }


                if (product.offerPercentage == null)
                    tvNewPrice.visibility = View.GONE
                tvPrice.text = "$ ${product.price}"


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
    val differ = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestProductsViewHolder {
        val itemBinding: LayoutProductItemBinding = LayoutProductItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return BestProductsViewHolder(itemBinding)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: BestProductsViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(differ.currentList[position])
        }
    }

    var onItemClick: ((Product) -> Unit)? = null
}