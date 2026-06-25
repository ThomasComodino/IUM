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
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AdminCouponsFragment : Fragment() {
    private var _binding: FragmentAdminCouponsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAdminCouponsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateUI()

        // Toggle Sconto Pasta
        binding.btnTogglePasta.setOnClickListener {
            val currentlyActive = FakeRepository.isPastaCouponPublished
            val action = if (currentlyActive) "disattivare" else "attivare"
            
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Conferma Azione")
                .setMessage("Vuoi davvero $action lo Sconto Pasta?")
                .setNegativeButton("Annulla", null)
                .setPositiveButton("Conferma") { _, _ ->
                    FakeRepository.isPastaCouponPublished = !currentlyActive
                    updateUI()
                    val msg = if (FakeRepository.isPastaCouponPublished) "Sconto Pasta attivato!" else "Sconto Pasta disattivato!"
                    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                }
                .show()
        }

        // Toggle Regalo Benvenuto
        binding.btnToggleGift.setOnClickListener {
            val currentlyActive = FakeRepository.isGiftCouponPublished
            val action = if (currentlyActive) "disattivare" else "attivare"

            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Conferma Azione")
                .setMessage("Vuoi davvero $action il Regalo di Benvenuto?")
                .setNegativeButton("Annulla", null)
                .setPositiveButton("Conferma") { _, _ ->
                    FakeRepository.isGiftCouponPublished = !currentlyActive
                    updateUI()
                    val msg = if (FakeRepository.isGiftCouponPublished) "Regalo attivato!" else "Regalo disattivato!"
                    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                }
                .show()
        }

        // Toggle Sconto Negozio
        binding.btnToggleShop.setOnClickListener {
            val currentlyActive = FakeRepository.isShopOnlyCouponPublished
            val action = if (currentlyActive) "disattivare" else "attivare"

            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Conferma Azione")
                .setMessage("Vuoi davvero $action lo Sconto Negozio?")
                .setNegativeButton("Annulla", null)
                .setPositiveButton("Conferma") { _, _ ->
                    FakeRepository.isShopOnlyCouponPublished = !currentlyActive
                    updateUI()
                    val msg = if (FakeRepository.isShopOnlyCouponPublished) "Sconto Negozio attivato!" else "Sconto Negozio disattivato!"
                    Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
                }
                .show()
        }
    }

    private fun updateUI() {
        // Aggiorna Pasta
        if (FakeRepository.isPastaCouponPublished) {
            binding.tvPastaStatus.text = "STATO: ATTIVO (Visibile)"
            binding.tvPastaStatus.setTextColor(resources.getColor(R.color.green_super))
            binding.btnTogglePasta.text = "DISATTIVA"
            binding.btnTogglePasta.setBackgroundColor(resources.getColor(R.color.red_error))
        } else {
            binding.tvPastaStatus.text = "STATO: DISATTIVATO (Nascosto)"
            binding.tvPastaStatus.setTextColor(resources.getColor(R.color.red_error))
            binding.btnTogglePasta.text = "ATTIVA"
            binding.btnTogglePasta.setBackgroundColor(resources.getColor(R.color.green_super))
        }

        // Aggiorna Gift
        if (FakeRepository.isGiftCouponPublished) {
            binding.tvGiftStatus.text = "STATO: ATTIVO (Visibile)"
            binding.tvGiftStatus.setTextColor(resources.getColor(R.color.green_super))
            binding.btnToggleGift.text = "DISATTIVA"
            binding.btnToggleGift.setBackgroundColor(resources.getColor(R.color.red_error))
        } else {
            binding.tvGiftStatus.text = "STATO: DISATTIVATO (Nascosto)"
            binding.tvGiftStatus.setTextColor(resources.getColor(R.color.red_error))
            binding.btnToggleGift.text = "ATTIVA"
            binding.btnToggleGift.setBackgroundColor(resources.getColor(R.color.green_super))
        }

        // Aggiorna Shop Only
        if (FakeRepository.isShopOnlyCouponPublished) {
            binding.tvShopStatus.text = "STATO: ATTIVO (Visibile)"
            binding.tvShopStatus.setTextColor(resources.getColor(R.color.green_super))
            binding.btnToggleShop.text = "DISATTIVA"
            binding.btnToggleShop.setBackgroundColor(resources.getColor(R.color.red_error))
        } else {
            binding.tvShopStatus.text = "STATO: DISATTIVATO (Nascosto)"
            binding.tvShopStatus.setTextColor(resources.getColor(R.color.red_error))
            binding.btnToggleShop.text = "ATTIVA"
            binding.btnToggleShop.setBackgroundColor(resources.getColor(R.color.green_super))
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
