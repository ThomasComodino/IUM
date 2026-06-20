package com.example.superspan.ui.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.content.Intent
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.superspan.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    // Questo è il segreto: il ViewBinding deve essere inizializzato!
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Qui colleghiamo il file XML fragment_profile
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ora puoi usare i tasti che hai messo nell'XML!
        binding.btnLogout.setOnClickListener {
            com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
                .setTitle("Logout")
                .setMessage("Sei sicuro di voler uscire dal tuo profilo?")
                .setNegativeButton("Annulla", null) // Se clicca annulla, non succede nulla
                .setPositiveButton("Esci") { _, _ ->
                    Toast.makeText(requireContext(), "Sessione terminata", Toast.LENGTH_SHORT).show()
                    val intent = Intent(requireContext(), com.example.superspan.ui.auth.LoginActivity::class.java)
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