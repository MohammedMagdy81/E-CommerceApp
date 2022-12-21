package com.example.e_commerce.adapters

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce.databinding.ColorsItemRvBinding

class ColorsAdapter : RecyclerView.Adapter<ColorsAdapter.ColorsViewHolder>() {

    private var selectedPosition = -1

    private val diffUtil = object : DiffUtil.ItemCallback<Int>() {
        override fun areItemsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Int, newItem: Int): Boolean {
            return oldItem == newItem
        }

    }

    val differList = AsyncListDiffer(this, diffUtil)

    inner class ColorsViewHolder(val binding: ColorsItemRvBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(color: Int, position: Int) {

            val imageDrawable = ColorDrawable(color)
            binding.imageColor.setImageDrawable(imageDrawable)

            if (position == selectedPosition) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorsViewHolder {
        val binding: ColorsItemRvBinding =
            ColorsItemRvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ColorsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ColorsViewHolder, position: Int) {
        val color = differList.currentList.get(position)
        holder.bind(color, position)

        holder.itemView.setOnClickListener {
            if (selectedPosition >= 0)
                notifyItemChanged(selectedPosition)
            selectedPosition = holder.adapterPosition
            notifyItemChanged(selectedPosition)
            onItemClick?.invoke(color)
        }
    }

    var onItemClick: ((Int) -> Unit)? = null

    override fun getItemCount(): Int = differList.currentList.size
}

















