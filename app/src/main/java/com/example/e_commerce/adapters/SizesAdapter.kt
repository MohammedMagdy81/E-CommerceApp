package com.example.e_commerce.adapters

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce.databinding.ColorsItemBinding
import com.example.e_commerce.databinding.SizesItemBinding

class SizesAdapter : RecyclerView.Adapter<SizesAdapter.SizesViewHolder>() {

    private var selectedPosition = -1

    inner class SizesViewHolder(val binding: SizesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(size: String, position: Int) {
            binding.tvSize.text = size
            if (position == selectedPosition) {
                binding.imageShadow.visibility = View.VISIBLE
            } else {
                binding.imageShadow.visibility = View.INVISIBLE
            }
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
    val diffList = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SizesViewHolder {
        val itemBinding: SizesItemBinding = SizesItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return SizesViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: SizesViewHolder, position: Int) {
        holder.bind(diffList.currentList[position], position)
        holder.itemView.setOnClickListener {
            if (selectedPosition >= 0)
                notifyItemChanged(selectedPosition)
            selectedPosition = holder.adapterPosition
            onItemClick?.invoke(diffList.currentList[position])
        }
    }

    override fun getItemCount(): Int = diffList.currentList.size

    var onItemClick: ((String) -> Unit)? = null
}



