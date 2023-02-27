package com.example.e_commerce.adapters

import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.e_commerce.R
import com.example.e_commerce.data.order.Order
import com.example.e_commerce.data.order.OrderStatus
import com.example.e_commerce.data.order.getOrderStatus
import com.example.e_commerce.databinding.OrderItemBinding

class OrdersAdapter : RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder>() {

    inner class OrdersViewHolder(val binding: OrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            binding.apply {
                orderId.text = order.orderId.toString()
                orderItemDate.text = order.date
                val resources = itemView.resources

                val colorDrawable = when (getOrderStatus(order.status)) {
                    OrderStatus.Canceled -> {
                        ColorDrawable(resources.getColor(R.color.g_red))
                    }
                    OrderStatus.Confirmed -> {
                        ColorDrawable(resources.getColor(br.com.simplepass.loadingbutton.R.color.green))
                    }
                    OrderStatus.Delivered -> {
                        ColorDrawable(resources.getColor(R.color.g_blue))
                    }
                    OrderStatus.Ordered -> {
                        ColorDrawable(resources.getColor(R.color.g_gray700))
                    }
                    OrderStatus.Returned -> {
                        ColorDrawable(resources.getColor(R.color.g_orange_yellow))
                    }
                    OrderStatus.Shipped -> {
                        ColorDrawable(resources.getColor(R.color.orange_color))
                    }
                }
                imageOrderState.setImageDrawable(colorDrawable)

            }
        }

    }

    private val diffUtil = object : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.orderId == newItem.orderId
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }

    }
    val differList = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        val binding: OrderItemBinding =
            OrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OrdersViewHolder(binding)
    }

    override fun getItemCount(): Int = differList.currentList.size

    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {
        val order = differList.currentList[position]
        holder.bind(order)
        holder.itemView.setOnClickListener {
            onOrderClick?.invoke(order)
        }

    }

    var onOrderClick: ((Order) -> Unit)? = null
}