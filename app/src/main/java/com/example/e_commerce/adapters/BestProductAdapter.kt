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
import com.example.e_commerce.databinding.ProductRvItemBinding
import kotlinx.coroutines.flow.DEFAULT_CONCURRENCY

class BestProductAdapter : RecyclerView.Adapter<BestProductAdapter.BestProductViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem == newItem
        }


    }

    var onClick:((Product)->Unit)?=null

    val differ = AsyncListDiffer(this, diffCallback)

    class BestProductViewHolder(val binding: ProductRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(product: Product) {
            binding.apply {
                Glide.with(imgProduct).load(product.images[0]).into(imgProduct)
                tvName.text = product.name
                product.offerPercentage?.let {
                    val remainingPrice = 1f - it
                    val newPrice = remainingPrice * product.price
                    tvNewPrice.text = "$ ${newPrice}"
                    tvPrice.paintFlags=Paint.STRIKE_THRU_TEXT_FLAG
                }
                if (product.offerPercentage==null){
                    tvNewPrice.visibility= View.INVISIBLE
                }
                tvPrice.text = "$ ${product.price}"


            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BestProductViewHolder {
        return BestProductViewHolder(
            ProductRvItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: BestProductViewHolder, position: Int) {
        val product = differ.currentList.get(position)
        holder.bind(product)
        holder.itemView.setOnClickListener {
            onClick?.invoke(product)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


}