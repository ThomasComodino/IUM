package com.example.superspan.ui.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.superspan.adapters.CartAdapter
import com.example.superspan.data.FakeRepository
import com.example.superspan.databinding.FragmentCartBinding
import androidx.navigation.fragment.findNavController
import com.example.superspan.R

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CartAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configura la RecyclerView a forma di lista verticale
        binding.rvCart.layoutManager = LinearLayoutManager(requireContext())

        // Passiamo i dati dal finto DB. La funzione "onQuantityChange" aggiorna il testo del totale!
        adapter = CartAdapter(FakeRepository.cart) {
            updateTotal()
        }
        binding.rvCart.adapter = adapter

        // Aggiorniamo il totale appena si apre la pagina
        updateTotal()

        // Click su Procedi
        binding.btnCheckout.setOnClickListener {
            if (FakeRepository.cart.isEmpty()) {
                Toast.makeText(requireContext(), "Il carrello è vuoto!", Toast.LENGTH_SHORT).show()
            } else {
                findNavController().navigate(R.id.action_cartFragment_to_checkoutFragment)
                // Al prossimo step faremo la navigazione vera alla mappa!
            }
        }
    }

    // Funzione che ricalcola il totale chiamando il nostro FakeRepository
    private fun updateTotal() {
        val total = FakeRepository.getTotal()
        binding.tvTotalPrice.text = "€ " + String.format("%.2f", total)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}