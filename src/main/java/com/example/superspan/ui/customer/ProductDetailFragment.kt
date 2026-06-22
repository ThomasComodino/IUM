package com.example.superspan.ui.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.superspan.data.FakeRepository
import com.example.superspan.databinding.FragmentProductDetailBinding

class ProductDetailFragment : Fragment() {
    private var _binding: FragmentProductDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProductDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Recuperiamo l'ID inviato dalla Home
        val productId = arguments?.getInt("productId") ?: -1
        val product = FakeRepository.products.find { it.id == productId }

        // Riempiamo la grafica
        product?.let {
            binding.tvDetailName.text = it.name
            binding.tvDetailDesc.text = it.description
            binding.tvDetailPrice.text = "€ ${it.price}"

            binding.btnDetailAdd.setOnClickListener { _ ->
                FakeRepository.addToCart(it)
                activity?.onBackPressed() // Torna indietro dopo l'aggiunta
            }
        }
    }
}