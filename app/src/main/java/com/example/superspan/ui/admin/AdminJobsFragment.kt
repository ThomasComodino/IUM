package com.example.superspan.ui.admin

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.superspan.R
import com.example.superspan.adapters.JobAdapter
import com.example.superspan.data.FakeRepository
import com.example.superspan.databinding.FragmentAdminJobsBinding

class AdminJobsFragment : Fragment(R.layout.fragment_admin_jobs) {
    private var _binding: FragmentAdminJobsBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAdminJobsBinding.bind(view)

        // Colleghiamo l'adapter ai dati finti per Daniela
        val adapter = JobAdapter(FakeRepository.applications) { candidate ->
            val bundle = Bundle().apply {
                putString("name", candidate.candidateName)
            }

            findNavController().navigate(R.id.adminJobDetailFragment, bundle)
        }
        binding.rvJobs.layoutManager = LinearLayoutManager(requireContext())
        binding.rvJobs.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}