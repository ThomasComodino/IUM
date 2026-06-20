package com.example.superspan.ui.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.superspan.data.FakeRepository
import com.example.superspan.databinding.FragmentCouponBinding

class CouponFragment : Fragment() {
    private var _binding: FragmentCouponBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCouponBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. ALL'APERTURA: Controlla se il coupon era già stato attivato
        if (FakeRepository.isPastaCouponActive) {
            setCouponAsActivated()
        }

        // 2. AL CLICK: Salva lo stato nel Repository e aggiorna la grafica
        binding.layoutCoupon.btnActivate.setOnClickListener {
            FakeRepository.isPastaCouponActive = true // Salvataggio "eterno" (finché l'app è aperta)
            setCouponAsActivated()
            Toast.makeText(requireContext(), "Coupon Michele Attivato!", Toast.LENGTH_SHORT).show()
        }
    }

    // Funzione di comodo per cambiare la grafica
    private fun setCouponAsActivated() {
        binding.layoutCoupon.btnActivate.text = "ATTIVATO"
        binding.layoutCoupon.btnActivate.isEnabled = false
        binding.layoutCoupon.chipStatus.text = "ATTIVO"
        // Opzionale: cambiamo colore al chip per farlo risaltare
        binding.layoutCoupon.chipStatus.setChipBackgroundColorResource(android.R.color.darker_gray)
    }
}