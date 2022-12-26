package com.example.e_commerce.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce.databinding.SizesItemRvBinding

class SizesAdapter : RecyclerView.Adapter<SizesAdapter.SizesViewHolder>() {

    private var selectedPosition = -1
    private val diffUtil = object : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
            return oldItem == newItem
        }

    }
     val differList = AsyncListDiffer(this, diffUtil)

    inner class SizesViewHolder(val binding: SizesItemRvBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(size: String, position: Int) {
            binding.tvSize.text = size
            if (position == selectedPosition) {
                binding.apply {
                    imageShadowSizes.visibility = View.VISIBLE
                }
            } else {
                binding.apply {
                    imageShadowSizes.visibility = View.INVISIBLE
                }
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizesViewHolder {
        val binding: SizesItemRvBinding =
            SizesItemRvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SizesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SizesViewHolder, position: Int) {
        val size = differList.currentList.get(position)
        holder.bind(size, position)

        holder.itemView.setOnClickListener {
            if (selectedPosition >= 0)
                notifyItemChanged(selectedPosition)
            selectedPosition = holder.adapterPosition
            notifyItemChanged(selectedPosition)
            onItemClick?.invoke(size)
        }
    }

    var onItemClick: ((String) -> Unit)? = null

    override fun getItemCount(): Int = differList.currentList.size
}