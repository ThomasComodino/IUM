package com.example.superspan.ui.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.superspan.R
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

        product?.let { p ->
            // Riempiamo i campi base
            binding.tvDetailName.text = p.name
            binding.tvDetailDesc.text = p.description
            binding.chipCategory.text = p.category.uppercase()
            binding.ivDetailImage.setImageResource(p.imageUrl)

            // --- CALCOLO DINAMICO DEL PREZZO ---
            // Chiediamo al repository il prezzo finale (che dipende dai coupon attivi)
            val finalPrice = FakeRepository.getFinalPrice(p)

            // Prezzo finale (quello grande in verde)
            binding.tvFinalPrice.text = "€ ${String.format("%.2f", finalPrice)}"

            // Se il prezzo finale è più basso di quello base, mostriamo lo sconto
            if (finalPrice < p.price) {
                // Prezzo vecchio (barrato)
                binding.tvOldPrice.visibility = View.VISIBLE
                binding.tvOldPrice.text = "€ ${String.format("%.2f", p.price)}"
                binding.tvOldPrice.paintFlags = binding.tvOldPrice.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG

                // Messaggio di risparmio (arancione per Michele)
                binding.tvSavings.visibility = View.VISIBLE
                val savings = p.price - finalPrice
                binding.tvSavings.text = "Risparmi € ${String.format("%.2f", savings)} (Sconto Coupon)"
            } else {
                // Altrimenti nascondiamo i campi dello sconto
                binding.tvOldPrice.visibility = View.GONE
                binding.tvSavings.visibility = View.GONE
            }

            // Gestione Preferiti (Michele)
            // Verifichiamo se il prodotto è già tra i preferiti all'apertura della pagina
            updateFavoriteButtonText(p)

            binding.btnFavorite.setOnClickListener {
                FakeRepository.toggleFavorite(p)
                updateFavoriteButtonText(p) // Aggiorna il testo dopo il click
                
                // Feedback visivo (opzionale, mantenuto per coerenza)
                val isNowFavorite = FakeRepository.favorites.any { it.product.id == p.id }
                if (isNowFavorite) {
                    Toast.makeText(requireContext(), "Aggiunto ai preferiti!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Rimosso dai preferiti", Toast.LENGTH_SHORT).show()
                }
            }

            // Bottone aggiungi al carrello
            binding.btnDetailAdd.setOnClickListener {
                FakeRepository.addToCart(p)
                Toast.makeText(requireContext(), "${p.name} aggiunto al carrello!", Toast.LENGTH_SHORT).show()
            }

            // Tasto Indietro (per Claudia)
            binding.btnBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    // Funzione di supporto per non ripetere codice
    private fun updateFavoriteButtonText(p: com.example.superspan.data.Product) {
        val isFavorite = FakeRepository.favorites.any { it.product.id == p.id }
        if (isFavorite) {
            binding.btnFavorite.text = "RIMUOVI DAI PREFERITI"
        } else {
            binding.btnFavorite.text = "AGGIUNGI AI PREFERITI"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}