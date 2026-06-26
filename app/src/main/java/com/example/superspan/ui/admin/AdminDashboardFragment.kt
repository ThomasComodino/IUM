package com.example.superspan.ui.admin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.superspan.data.FakeRepository
import com.example.superspan.databinding.FragmentAdminDashboardBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AdminDashboardFragment : Fragment() {

    private var _binding: FragmentAdminDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAdminDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Aggiorna i dati reali del repository nelle card
        binding.tvOrdersCount.text = FakeRepository.orders.size.toString()
        binding.tvCouponsCount.text = FakeRepository.adminCoupons.count { it.isActive }.toString()
        binding.tvJobsCount.text = FakeRepository.applications.size.toString()

        binding.btnAdminLogout.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Logout Gestionale")
                .setMessage("Vuoi uscire dall'area amministratore?")
                .setNegativeButton("Annulla", null)
                .setPositiveButton("Esci") { _, _ ->
                    // Manteniamo le credenziali se "Ricordami" era attivo (come fatto nel profilo cliente)
                    val pref = requireContext().getSharedPreferences("SUperSpanPrefs", Context.MODE_PRIVATE)
                    val email = pref.getString("email", "")
                    val pass = pref.getString("pass", "")
                    
                    pref.edit().clear().apply()
                    
                    if (!email.isNullOrBlank()) {
                        pref.edit().putString("email", email).putString("pass", pass).apply()
                    }

                    val intent = Intent(requireContext(), com.example.superspan.ui.auth.LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    requireActivity().finish()
                }
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
