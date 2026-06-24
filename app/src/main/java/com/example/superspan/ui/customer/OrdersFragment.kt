package com.example.superspan.ui.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.superspan.R
import com.example.superspan.data.FakeRepository
import com.example.superspan.data.Order

class OrdersFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_orders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val rvOrders = view.findViewById<RecyclerView>(R.id.rvOrders)
        val tvNoOrders = view.findViewById<TextView>(R.id.tvNoOrders)

        if (FakeRepository.orders.isEmpty()) {
            rvOrders.visibility = View.GONE
            tvNoOrders.visibility = View.VISIBLE
        } else {
            rvOrders.visibility = View.VISIBLE
            tvNoOrders.visibility = View.GONE
            rvOrders.layoutManager = LinearLayoutManager(requireContext())
            rvOrders.adapter = OrdersAdapter(FakeRepository.orders)
        }
    }

    class OrdersAdapter(private val orders: List<Order>) : RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>() {
        
        class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvId: TextView = view.findViewById(R.id.tvOrderId)
            val tvStatus: TextView = view.findViewById(R.id.tvOrderStatus)
            val tvDate: TextView = view.findViewById(R.id.tvOrderDate)
            val tvTotal: TextView = view.findViewById(R.id.tvOrderTotal)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
            return OrderViewHolder(view)
        }

        override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
            val order = orders[position]
            holder.tvId.text = order.id
            holder.tvStatus.text = order.status
            holder.tvDate.text = order.date
            holder.tvTotal.text = "€ %.2f".format(order.total)
        }

        override fun getItemCount() = orders.size
    }
}