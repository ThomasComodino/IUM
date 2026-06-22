package com.example.superspan.ui.admin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.superspan.ui.auth.LoginActivity
import com.example.superspan.databinding.FragmentAdminDashboardBinding

/**
 * Schermata principale per Daniela (Admin).
 * Mostra le statistiche rapide dell'applicazione SuperSpan.
 */
class AdminDashboardFragment : Fragment() {

    private var _binding: FragmentAdminDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.cardActiveOffers.setOnClickListener {
            showToast("Visualizzazione Offerte Attive")
        }

        binding.cardRedeemedCoupons.setOnClickListener {
            showToast("Visualizzazione Coupon Riscossi")
        }

        binding.cardJobApplications.setOnClickListener {
            showToast("Visualizzazione Candidature Lavoro")
        }

        binding.cardOrdersToday.setOnClickListener {
            showToast("Visualizzazione Ordini di Oggi")
        }

        binding.btnAdminLogout.setOnClickListener {
            // Ritorna al Login
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
