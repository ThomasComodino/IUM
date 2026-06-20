package com.example.superspan.ui.admin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.superspan.databinding.FragmentAdminDashboardBinding

class AdminDashboardFragment : Fragment() {

    private var _binding: FragmentAdminDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Colleghiamo il file XML fragment_admin_dashboard
        _binding = FragmentAdminDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Logica del tasto Logout di Daniela
        binding.btnAdminLogout.setOnClickListener {
            com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
                .setTitle("Logout Gestionale")
                .setMessage("Vuoi uscire dall'area amministratore?")
                .setNegativeButton("Annulla", null)
                .setPositiveButton("Esci") { _, _ ->
                    // --- NUOVA LOGICA PER TORNARE AL LOGIN ---
                    val intent = Intent(requireContext(), com.example.superspan.ui.auth.LoginActivity::class.java)

                    // Queste due righe servono a "pulire" la memoria:
                    // impediscono all'utente di tornare indietro nell'area admin premendo il tasto back del telefono.
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                    startActivity(intent)
                }
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}