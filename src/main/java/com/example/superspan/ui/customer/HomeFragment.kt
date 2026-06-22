package com.example.superspan.ui.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.superspan.adapters.ProductAdapter
import com.example.superspan.data.FakeRepository
import com.example.superspan.databinding.FragmentHomeBinding
import androidx.navigation.fragment.findNavController
import com.example.superspan.R

class HomeFragment : Fragment() {

    // ViewBinding per accedere all'XML in modo sicuro
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Creiamo l'Adapter passando la nostra lista finta di prodotti
        val adapter = ProductAdapter(
            products = FakeRepository.products,
            onProductClick = { product ->
                // Creiamo un pacchetto (Bundle) con l'ID del prodotto selezionato
                val bundle = Bundle().apply {
                    putInt("productId", product.id)
                }
                // Navighiamo al dettaglio passando il pacchetto
                findNavController().navigate(R.id.productDetailFragment, bundle)
            },
            onAddClick = { product ->
                FakeRepository.addToCart(product)
                Toast.makeText(requireContext(), "${product.name} aggiunto!", Toast.LENGTH_SHORT).show()
            }
        )

        // Diciamo alla griglia di mostrare 2 prodotti per riga
        binding.rvProducts.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvProducts.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}