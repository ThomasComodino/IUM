package com.example.superspan.ui.customer

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    // Variabili di stato per filtri e ordinamento
    private var currentSearchText = ""
    private var currentCategory = "Tutti"
    private var onlyOffers = false
    private var isAscending = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ProductAdapter(
            products = FakeRepository.products,
            onProductClick = { product ->
                val bundle = Bundle().apply {
                    putInt("productId", product.id)
                }
                findNavController().navigate(R.id.productDetailFragment, bundle)
            },
            onAddClick = { product ->
                FakeRepository.addToCart(product)
                Toast.makeText(requireContext(), "${product.name} aggiunto!", Toast.LENGTH_SHORT).show()
            }
        )

        binding.rvProducts.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvProducts.adapter = adapter

        // 1. RICERCA PER NOME
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                currentSearchText = s.toString()
                applyFilters()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // 2. FILTRO CATEGORIE E OFFERTE
        binding.chipGroupFilters.setOnCheckedStateChangeListener { _, checkedIds ->
            val chipId = checkedIds.firstOrNull()
            onlyOffers = (chipId == R.id.chipOffers)
            currentCategory = when (chipId) {
                R.id.chipAlimentari -> "Alimentari"
                R.id.chipBevande -> "Bevande"
                else -> "Tutti"
            }
            applyFilters()
        }

        // 3. ORDINAMENTO PREZZO
        binding.btnSort.setOnClickListener {
            isAscending = !isAscending
            val msg = if (isAscending) "Prezzo: dal più basso" else "Prezzo: dal più alto"
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
            applyFilters()
        }
    }

    private fun applyFilters() {
        var list = FakeRepository.products

        if (currentSearchText.isNotEmpty()) {
            list = list.filter { it.name.contains(currentSearchText, ignoreCase = true) }
        }

        if (currentCategory != "Tutti") {
            list = list.filter { it.category == currentCategory }
        }

        if (onlyOffers) {
            list = list.filter { FakeRepository.getFinalPrice(it) < it.price }
        }

        list = if (isAscending) {
            list.sortedBy { FakeRepository.getFinalPrice(it) }
        } else {
            list.sortedByDescending { FakeRepository.getFinalPrice(it) }
        }

        (binding.rvProducts.adapter as ProductAdapter).updateList(list)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}