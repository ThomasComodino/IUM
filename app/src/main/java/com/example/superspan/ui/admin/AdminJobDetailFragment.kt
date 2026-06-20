package com.example.superspan.ui.admin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.superspan.R
import com.example.superspan.data.FakeRepository
import com.example.superspan.databinding.FragmentAdminJobDetailBinding

class AdminJobDetailFragment : Fragment(R.layout.fragment_admin_job_detail) {
    private var _binding: FragmentAdminJobDetailBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAdminJobDetailBinding.bind(view)

        // Riceviamo l'ID o il Nome (usiamo il nome per semplicità ora)
        val name = arguments?.getString("name") ?: ""
        val candidate = FakeRepository.applications.find { it.candidateName == name }

        candidate?.let {
            binding.tvDetailCandidateName.text = it.candidateName
            binding.tvDetailRole.text = it.role

            binding.btnApprove.setOnClickListener {
                Toast.makeText(requireContext(), "Candidato approvato. Email inviata!", Toast.LENGTH_LONG).show()
                activity?.onBackPressed()
            }
        }
    }
}