package com.example.e_commerce.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_commerce.databinding.ViewpagerImagesItemBinding

class ViewPagerImagesAdapter : RecyclerView.Adapter<ViewPagerImagesAdapter.ViewPagerViewHolder>() {

    inner class ViewPagerViewHolder(val binding: ViewpagerImagesItemBinding) :
        RecyclerView.ViewHolder(
            binding.root
        ) {
        fun bind(image: String?) {
            Glide.with(itemView).load(image).into(binding.viewPagerImage)
        }

    }

    val diffUtil = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }
    val differList = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerViewHolder {
        val binding: ViewpagerImagesItemBinding =
            ViewpagerImagesItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewPagerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewPagerViewHolder, position: Int) {
        val image = differList.currentList.get(position)
        holder.bind(image)
    }

    override fun getItemCount(): Int {
        return differList.currentList.size
    }
}




