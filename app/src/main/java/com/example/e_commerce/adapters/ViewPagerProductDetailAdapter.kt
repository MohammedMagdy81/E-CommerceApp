package com.example.e_commerce.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_commerce.databinding.ImageProductDetailItemBinding

class ViewPagerProductDetailAdapter :
    RecyclerView.Adapter<ViewPagerProductDetailAdapter.ViewPagerProductDetailHolder>() {

    inner class ViewPagerProductDetailHolder(val binding: ImageProductDetailItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(url: String) {
            Glide.with(itemView).load(url).centerInside().into(binding.imageProductDetail)
        }
    }

    private val diffUtil = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }
    }
    val differList = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewPagerProductDetailHolder {
        val itemBinding: ImageProductDetailItemBinding = ImageProductDetailItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewPagerProductDetailHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ViewPagerProductDetailHolder, position: Int) {
        holder.bind(differList.currentList[position])
    }

    override fun getItemCount(): Int = differList.currentList.size
}