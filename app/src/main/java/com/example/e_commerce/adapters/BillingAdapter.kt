package com.example.e_commerce.adapters

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_commerce.R
import com.example.e_commerce.data.Address
import com.example.e_commerce.data.CartProduct
import com.example.e_commerce.databinding.AddressRvItemBinding
import com.example.e_commerce.databinding.BillingProductRvItemBinding
import com.example.e_commerce.utils.getProductPrice

class BillingAdapter : RecyclerView.Adapter<BillingAdapter.BillingViewHolder>() {

    inner class BillingViewHolder(val binding: BillingProductRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cartProduct: CartProduct, isSelected: Boolean) {
            binding.apply {
                Glide.with(itemView).load(cartProduct.product.images.first())
                    .into(billingImageProduct)
                billingProductName.text = cartProduct.product.name
                billingProductQuantity.text = cartProduct.quantity.toString()
                val priceAfterDiscount =
                    cartProduct.product.offerPercentage.getProductPrice(cartProduct.product.price)
                billingProductPrice.text = "$ ${String.format("%.2f", priceAfterDiscount)}"
                cartProduct.selectedSize?.let {
                    billingSelectSize.text = it
                }
                billingProductSelectColor.setImageDrawable(
                    ColorDrawable(
                        cartProduct.selectedColor ?: Color.TRANSPARENT
                    )
                )


            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillingViewHolder {
        val itemBinding: BillingProductRvItemBinding =
            BillingProductRvItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BillingViewHolder(itemBinding)
    }

    override fun getItemCount(): Int {
        return differList.currentList.size
    }

    var selectedAddress = -1
    override fun onBindViewHolder(holder: BillingViewHolder, position: Int) {
        val cartProduct = differList.currentList.get(position)
        holder.bind(cartProduct, selectedAddress == position)


    }


    private val diffUtil = object : DiffUtil.ItemCallback<CartProduct>() {
        override fun areItemsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return newItem.product.id == oldItem.product.id
        }

        override fun areContentsTheSame(oldItem: CartProduct, newItem: CartProduct): Boolean {
            return oldItem == newItem
        }

    }
    var differList = AsyncListDiffer(this, diffUtil)

    var onItemClick: ((CartProduct) -> Unit)? = null
}

