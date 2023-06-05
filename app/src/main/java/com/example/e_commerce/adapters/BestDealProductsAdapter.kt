package com.example.e_commerce.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_commerce.data.Product
import com.example.e_commerce.databinding.LayoutBestdealProductItemBinding
import com.example.e_commerce.databinding.LayoutSpecialProductItemBinding

class BestDealProductsAdapter :
    RecyclerView.Adapter<BestDealProductsAdapter.BestDealProductsViewHolder>() {

    inner class BestDealProductsViewHolder(val binding: LayoutBestdealProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.apply {
                Glide.with(itemView).load(product.images.first()).centerInside().into(bestDealImg)
                bestDealProductName.text = product.name
                product.offerPercentage?.let {
                    val remainingPercentage = 1f - it
                    val priceAfterDiscount = remainingPercentage * product.price
                    bestDealNewPrice.text = "$ ${String.format("%.2f", priceAfterDiscount)}"
                    bestDealOldPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                }
                bestDealOldPrice.text = "$ ${product.price}"

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestDealProductsViewHolder {
        val itemBinding: LayoutBestdealProductItemBinding =
            LayoutBestdealProductItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return BestDealProductsViewHolder(itemBinding)
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: BestDealProductsViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(differ.currentList[position])
        }
    }

    var onItemClick: ((Product) -> Unit)? = null
}