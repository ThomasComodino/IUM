package com.example.superspan.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.superspan.R
import com.example.superspan.adapters.AdminJobOffersAdapter
import com.example.superspan.adapters.JobAdapter
import com.example.superspan.data.FakeRepository
import com.example.superspan.data.JobOffer
import com.example.superspan.databinding.DialogAddJobOfferBinding
import com.example.superspan.databinding.FragmentAdminJobsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout

class AdminJobsFragment : Fragment() {
    private var _binding: FragmentAdminJobsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAdminJobsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView(0) // Start with Applications

        binding.tabLayoutJobs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                setupRecyclerView(tab?.position ?: 0)
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        binding.chipGroupFilters.setOnCheckedStateChangeListener { _, checkedIds ->
            val filter = when(checkedIds.firstOrNull()) {
                R.id.chipBovisa -> "Milano Bovisa"
                R.id.chipCentral -> "Milano Central"
                else -> "Tutte"
            }
            applyApplicationFilter(filter)
        }

        binding.fabAddJobOffer.setOnClickListener {
            showAddJobOfferDialog()
        }
    }

    private fun setupRecyclerView(position: Int) {
        binding.rvJobs.layoutManager = LinearLayoutManager(requireContext())
        if (position == 0) {
            // Applications
            binding.fabAddJobOffer.visibility = View.GONE
            binding.hsvFilters.visibility = View.VISIBLE
            applyApplicationFilter("Tutte")
        } else {
            // Job Offers
            binding.fabAddJobOffer.visibility = View.VISIBLE
            binding.hsvFilters.visibility = View.GONE
            val adapter = AdminJobOffersAdapter(FakeRepository.jobOffers)
            binding.rvJobs.adapter = adapter
        }
    }

    private fun applyApplicationFilter(location: String) {
        val filteredList = if (location == "Tutte") {
            FakeRepository.applications
        } else {
            FakeRepository.applications.filter { it.location == location }
        }

        val adapter = JobAdapter(filteredList) { candidate ->
            val bundle = Bundle().apply {
                putString("name", candidate.candidateName)
            }
            findNavController().navigate(R.id.adminJobDetailFragment, bundle)
        }
        binding.rvJobs.adapter = adapter
    }

    private fun showAddJobOfferDialog() {
        val dialogBinding = DialogAddJobOfferBinding.inflate(layoutInflater)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Nuova Posizione")
            .setView(dialogBinding.root)
            .setNegativeButton("Annulla", null)
            .setPositiveButton("Pubblica") { _, _ ->
                val title = dialogBinding.etJobTitle.text.toString()
                val location = dialogBinding.etJobLocation.text.toString()
                val contract = dialogBinding.etContractType.text.toString()
                val desc = dialogBinding.etJobDescription.text.toString()

                if (title.isNotBlank() && location.isNotBlank() && contract.isNotBlank() && desc.isNotBlank()) {
                    val newId = (FakeRepository.jobOffers.maxOfOrNull { it.id } ?: 0) + 1
                    FakeRepository.jobOffers.add(0, JobOffer(newId, title, desc, location, contract))
                    setupRecyclerView(1)
                    Toast.makeText(requireContext(), "Posizione pubblicata con successo", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Compila tutti i campi!", Toast.LENGTH_SHORT).show()
                }
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
