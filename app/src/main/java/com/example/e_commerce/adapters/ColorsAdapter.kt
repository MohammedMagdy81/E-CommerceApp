package com.example.e_commerce.adapters

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce.databinding.ColorsItemBinding

class ColorsAdapter : RecyclerView.Adapter<ColorsAdapter.ColorsVwHolder>() {

    private var selectedPosition = -1

    inner class ColorsVwHolder(val binding: ColorsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(color: Int, position: Int) {
            val colorDrawable = ColorDrawable(color)
            binding.imageColor.setImageDrawable(colorDrawable)

            if (selectedPosition == position) {
                binding.apply {
                    imageShadow.visibility = View.VISIBLE
                    imageCheck.visibility = View.VISIBLE
                }
            } else {
                binding.apply {
                    imageShadow.visibility = View.INVISIBLE
                    imageCheck.visibility = View.INVISIBLE
                }
            }

        }

    }

    private val diffUtil = object : DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

    }
    val diffList = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorsVwHolder {
        val itemBinding: ColorsItemBinding = ColorsItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ColorsVwHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: ColorsVwHolder, position: Int) {
        holder.bind(diffList.currentList[position], position)
        holder.itemView.setOnClickListener {
            if (selectedPosition >= 0)
                notifyItemChanged(selectedPosition)

            selectedPosition = holder.adapterPosition
            notifyItemChanged(selectedPosition)

            onItemClick?.invoke(diffList.currentList[position])
        }
    }

    override fun getItemCount(): Int = diffList.currentList.size

    var onItemClick: ((Int) -> Unit)? = null
}



