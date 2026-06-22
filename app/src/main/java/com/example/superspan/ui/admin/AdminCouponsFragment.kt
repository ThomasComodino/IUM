package com.example.superspan.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.superspan.R
import com.example.superspan.data.FakeRepository
import com.example.superspan.databinding.FragmentAdminCouponsBinding

class AdminCouponsFragment : Fragment() {
    private var _binding: FragmentAdminCouponsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAdminCouponsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Sincronizza lo stato all'apertura
        updateUI()

        // Daniela clicca su PUBBLICA
        binding.btnAdminActivate.setOnClickListener {
            FakeRepository.isCouponPublishedByAdmin = true
            updateUI()
            Toast.makeText(requireContext(), "Coupon inviato all'app clienti!", Toast.LENGTH_SHORT).show()
        }

        // Daniela clicca su NASCONDI
        binding.btnAdminDeactivate.setOnClickListener {
            FakeRepository.isCouponPublishedByAdmin = false
            updateUI()
            Toast.makeText(requireContext(), "Coupon rimosso dal mercato", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateUI() {
        if (FakeRepository.isCouponPublishedByAdmin) {
            binding.tvAdminStatus.text = "STATO: PUBBLICATO (Visibile)"
            binding.tvAdminStatus.setTextColor(resources.getColor(R.color.green_super))
            binding.btnAdminActivate.isEnabled = false // Già attivo
            binding.btnAdminDeactivate.isEnabled = true
        } else {
            binding.tvAdminStatus.text = "STATO: NASCOSTO (Non visibile)"
            binding.tvAdminStatus.setTextColor(resources.getColor(R.color.red_error))
            binding.btnAdminActivate.isEnabled = true
            binding.btnAdminDeactivate.isEnabled = false // Già nascosto
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}