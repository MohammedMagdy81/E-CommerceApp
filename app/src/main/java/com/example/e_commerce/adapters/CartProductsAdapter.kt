package com.example.e_commerce.adapters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_commerce.data.CartProduct
import com.example.e_commerce.databinding.CartProductItemBinding
import com.example.e_commerce.utils.getProductPrice

class CartProductsAdapter :
    RecyclerView.Adapter<CartProductsAdapter.CartProductsViewHolder>() {

    inner class CartProductsViewHolder(val binding: CartProductItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cartProduct: CartProduct) {
            binding.apply {
                Glide.with(itemView).load(cartProduct.product.images[0]).into(cartItemImage)
                cartItemName.text = cartProduct.product.name
                cartItemNumber.text = cartProduct.quantity.toString()

                val priceAfterPercentage =
                    cartProduct.product.offerPercentage.getProductPrice(cartProduct.product.price)

                cartItemPrice.text="$ ${String.format("%.2f", priceAfterPercentage)}"

                cartProduct.selectedSize?.let {
                    tvCartProductSize.text = it
                }
                cartItemSelectedColor.setImageDrawable(
                    ColorDrawable(
                        cartProduct.selectedColor ?: Color.TRANSPARENT
                    )
                )


            }
        }

    }

    val diffUtil = object : DiffUtil.ItemCallback<CartProduct>() {
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem.product.id == newItem.product.id
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartProductsViewHolder {
        val itemBinding: CartProductItemBinding =
            CartProductItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return CartProductsViewHolder(itemBinding)
    }

    val differ = AsyncListDiffer(this, diffUtil)

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: CartProductsViewHolder, position: Int) {
        val cartProduct = differ.currentList[position]
        holder.bind(cartProduct)
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(cartProduct)
        }
        holder.binding.cartItemPlus.setOnClickListener {
            onItemPlusClick?.invoke(cartProduct)
        }
        holder.binding.cartItemMinus.setOnClickListener {
            onItemMinusClick?.invoke(cartProduct)
        }

    }

    var onItemClick: ((CartProduct) -> Unit)? = null
    var onItemPlusClick: ((CartProduct) -> Unit)? = null
    var onItemMinusClick: ((CartProduct) -> Unit)? = null
}