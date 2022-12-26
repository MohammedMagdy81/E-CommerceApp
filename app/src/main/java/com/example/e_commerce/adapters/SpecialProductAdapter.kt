package com.example.e_commerce.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_commerce.data.Product
import com.example.e_commerce.databinding.SpecialRvItemBinding

class SpecialProductAdapter : RecyclerView.Adapter<SpecialProductAdapter.SpecialViewHolder>() {


    private val diffCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffCallback)


    class SpecialViewHolder(val binding: SpecialRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.apply {
                Glide.with(imgAd).load(product.images[0]).into(imgAd)
                tvAdName.text = product.name
                tvAdPrice.text = product.price.toString() + " EG "


            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialViewHolder {
        return SpecialViewHolder(
            SpecialRvItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SpecialViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)
        holder.itemView.setOnClickListener {
            onClick?.invoke(product)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }
    var onClick:((Product)->Unit)?=null
}









