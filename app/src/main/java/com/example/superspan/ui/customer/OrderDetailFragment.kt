package com.example.superspan.ui.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.superspan.R
import com.example.superspan.adapters.CartAdapter
import com.example.superspan.data.FakeRepository
import com.example.superspan.databinding.FragmentOrderDetailBinding

class OrderDetailFragment : Fragment() {
    private var _binding: FragmentOrderDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOrderDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val orderId = arguments?.getString("orderId") ?: ""
        val order = FakeRepository.orders.find { it.id == orderId }

        order?.let { o ->
            binding.tvOrderIdDetail.text = "Ordine: ${o.id}"
            binding.tvOrderDateDetail.text = "Data: ${o.date}"
            binding.tvOrderStatusDetail.text = "Stato: ${o.status}"
            binding.tvOrderAddressDetail.text = o.deliveryAddress
            binding.tvOrderTotalDetail.text = "€ %.2f".format(o.total)

            binding.rvOrderItems.layoutManager = LinearLayoutManager(requireContext())
            binding.rvOrderItems.adapter = CartAdapter(o.items, {}, {}, isReadOnly = true)

            binding.btnBackOrder.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
