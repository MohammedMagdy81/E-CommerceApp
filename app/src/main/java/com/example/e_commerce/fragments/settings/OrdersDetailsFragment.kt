package com.example.e_commerce.fragments.settings

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.e_commerce.R
import com.example.e_commerce.adapters.BillingAdapter
import com.example.e_commerce.data.order.Order
import com.example.e_commerce.data.order.OrderStatus
import com.example.e_commerce.data.order.getOrderStatus
import com.example.e_commerce.databinding.FragmentOrderDetailsBinding
import com.example.e_commerce.utils.HorizontalItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import es.dmoral.toasty.Toasty
import java.util.*

@AndroidEntryPoint
class OrdersDetailsFragment : Fragment() {
    private lateinit var binding: FragmentOrderDetailsBinding

    private val billingAdapter by lazy {
        BillingAdapter()
    }

    private val args by navArgs<OrdersDetailsFragmentArgs>()
    private var order: Order? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        order = args.order
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrderDetailsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (order?.status == OrderStatus.Delivered.status) {
            binding.celebrationAnim.visibility = View.VISIBLE
        }
        setupStepView()
        setupProductsRv()
        setupAllOrderData()
    }

    private fun setupAllOrderData() {
        binding.apply {
            tvOrderId.text = "Order #${order?.orderId}"
            tvAddress.text = "${order?.address?.street} - ${order?.address?.city}"
            tvPhoneNumber.text = order?.address?.phone
            tvFullName.text = order?.address?.fullName
            tvShoppingAddresses.text = "${order?.address?.addressTitle}"
            tvTotalprice.text = requireContext().getString(R.string.dollar) + "  " + String.format(
                Locale.getDefault(),
                "%.2f",
                order?.totalPrice
            )

            billingAdapter.differList.submitList(order!!.products)


        }
    }

    private fun setupProductsRv() {
        binding.rvProducts.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            addItemDecoration(HorizontalItemDecoration(10))
            adapter = billingAdapter
        }
    }

    private fun setupStepView() {
        val state = when (args.order.status) {
            "Ordered" -> 1
            "Confirmed" -> 2
            "Shipped" -> 3
            "Delivered" -> 4
            else -> {
                2
            }
        }


        Log.d("test2", state.toString())
        val steps = arrayOf<String>(
            resources.getText(R.string.g_order_placed).toString(),
            resources.getText(R.string.g_confirm).toString(),
            resources.getText(R.string.g_shipped).toString(),
            resources.getText(R.string.g_delivered).toString()
        )

        binding.stepView.apply {
            getState().stepsNumber(4)
                .steps(steps.toMutableList())
                .commit()
            if (state == 4) {
                go(3, true)
                done(true)
            } else {
                go(state, true)
            }

        }
    }
}