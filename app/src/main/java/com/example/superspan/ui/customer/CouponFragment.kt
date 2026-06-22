package com.example.superspan.ui.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.superspan.R
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

        // --- CONTROLLO DANIELA (ADMIN) ---
        // Se Daniela ha deciso di non pubblicare il coupon, lo nascondiamo a Michele
        if (!FakeRepository.isCouponPublishedByAdmin) {
            binding.layoutCoupon.root.visibility = View.GONE
            binding.tvNoCoupons.visibility = View.VISIBLE // Assicurati di avere questo ID nell'XML
        } else {
            binding.layoutCoupon.root.visibility = View.VISIBLE
            binding.tvNoCoupons.visibility = View.GONE

            // 1. ALL'APERTURA: Controlla se Michele lo aveva già attivato
            if (FakeRepository.isPastaCouponActive) {
                setCouponAsActivated()
            }

            // 2. AL CLICK: Michele attiva lo sconto per se stesso
            binding.layoutCoupon.btnActivate.setOnClickListener {
                FakeRepository.isPastaCouponActive = true
                setCouponAsActivated()
                Toast.makeText(requireContext(), "Coupon attivato! Sconto pronto.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setCouponAsActivated() {
        binding.layoutCoupon.btnActivate.text = "ATTIVATO"
        binding.layoutCoupon.btnActivate.isEnabled = false
        binding.layoutCoupon.chipStatus.text = "ATTIVO"
        binding.layoutCoupon.chipStatus.setChipBackgroundColorResource(android.R.color.darker_gray)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}