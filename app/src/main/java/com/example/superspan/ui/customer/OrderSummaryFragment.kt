package com.example.superspan.ui.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.superspan.R
import com.example.superspan.data.FakeRepository
import com.google.android.material.button.MaterialButton

class OrderSummaryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_order_summary, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val total = FakeRepository.getTotal()
        view.findViewById<TextView>(R.id.tvSummaryTotal).text = "€ " + String.format("%.2f", total)

        view.findViewById<MaterialButton>(R.id.btnFinalConfirm).setOnClickListener {
            // Svuota il carrello
            FakeRepository.cart.clear()
            
            Toast.makeText(requireContext(), "Ordine Confermato e Pagato!", Toast.LENGTH_LONG).show()
            
            // Torna alla Home e pulisce il backstack del checkout
            findNavController().navigate(R.id.action_orderSummaryFragment_to_homeFragment)
        }
    }
}