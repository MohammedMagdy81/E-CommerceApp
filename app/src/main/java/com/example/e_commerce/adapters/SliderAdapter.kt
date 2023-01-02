package com.example.e_commerce.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.e_commerce.databinding.ViewpagerImagesItemBinding
import com.smarteist.autoimageslider.SliderViewAdapter

class SliderAdapter(private val images: List<String>) :
    SliderViewAdapter<SliderAdapter.SliderViewHolder>() {

    inner class SliderViewHolder(val binding: ViewpagerImagesItemBinding) :
        SliderViewAdapter.ViewHolder(binding.root) {
        fun bind(image: String) {
            Glide.with(binding.root)
                .load(image)
                .centerCrop()
                .into(binding.viewPagerImage);
        }

    }

    override fun getCount(): Int {
        return images.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?): SliderViewHolder {
        val itemBinding: ViewpagerImagesItemBinding =
            ViewpagerImagesItemBinding.inflate(LayoutInflater.from(parent?.context), parent, false)
        return SliderViewHolder(itemBinding)
    }

    override fun onBindViewHolder(viewHolder: SliderViewHolder?, position: Int) {
        val image = images.get(position)
        viewHolder?.bind(image)
    }

}