package com.example.superspan.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
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
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout

class AdminJobsFragment : Fragment() {
    private var _binding: FragmentAdminJobsBinding? = null
    private val binding get() = _binding!!

    // Liste predefinite per Daniela
    private val roles = listOf("Cassiera", "Scaffalista", "Magazziniere", "Responsabile Reparto")
    private val locations = listOf("Milano Central", "Milano Bovisa", "Milano Loreto", "Sesto S.G.")
    private val contracts = listOf("Determinato", "Indeterminato", "Apprendistato", "Part-Time")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAdminJobsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView(0)

        binding.tabLayoutJobs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                setupRecyclerView(tab?.position ?: 0)
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        binding.fabAddJobOffer.setOnClickListener {
            showAddJobOfferDialog()
        }
    }

    private fun setupRecyclerView(position: Int) {
        binding.rvJobs.layoutManager = LinearLayoutManager(requireContext())
        if (position == 0) {
            // Applications
            binding.layoutAddBtn.visibility = View.GONE
            binding.hsvFilters.visibility = View.VISIBLE
            setupApplicationFilters()
            applyApplicationFilter(null)
        } else {
            // Job Offers
            binding.layoutAddBtn.visibility = View.VISIBLE
            binding.hsvFilters.visibility = View.GONE
            val adapter = AdminJobOffersAdapter(FakeRepository.jobOffers)
            binding.rvJobs.adapter = adapter
        }
    }

    private fun setupApplicationFilters() {
        val count = binding.chipGroupFilters.childCount
        if (count > 1) {
            for (i in count - 1 downTo 1) {
                binding.chipGroupFilters.removeViewAt(i)
            }
        }

        // Filtro basato sui RUOLI delle candidature esistenti
        val rolesInApps = FakeRepository.applications.map { it.role }.distinct()
        
        rolesInApps.forEach { role ->
            val chip = layoutInflater.inflate(R.layout.item_filter_chip, binding.chipGroupFilters, false) as Chip
            chip.text = role
            chip.id = View.generateViewId()
            chip.tag = role
            binding.chipGroupFilters.addView(chip)
        }

        binding.chipGroupFilters.setOnCheckedStateChangeListener { group, checkedIds ->
            val checkedId = checkedIds.firstOrNull()
            if (checkedId == null || checkedId == R.id.chipAll) {
                applyApplicationFilter(null)
            } else {
                val chip = group.findViewById<Chip>(checkedId)
                val roleTag = chip.tag as String
                applyApplicationFilter(roleTag)
            }
        }
    }

    private fun applyApplicationFilter(role: String?) {
        val filteredList = if (role == null) {
            FakeRepository.applications
        } else {
            FakeRepository.applications.filter { it.role == role }
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

        // Setup Dropdown con liste fisse
        val roleAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, roles)
        dialogBinding.autoCompleteRole.setAdapter(roleAdapter)

        val locAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, locations)
        dialogBinding.autoCompleteLocation.setAdapter(locAdapter)

        val contractAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, contracts)
        dialogBinding.autoCompleteContract.setAdapter(contractAdapter)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Nuova Posizione")
            .setView(dialogBinding.root)
            .setNegativeButton("Annulla", null)
            .setPositiveButton("Pubblica") { _, _ ->
                val title = dialogBinding.autoCompleteRole.text.toString()
                val location = dialogBinding.autoCompleteLocation.text.toString()
                val contract = dialogBinding.autoCompleteContract.text.toString()
                val desc = dialogBinding.etJobDescription.text.toString()

                if (title.isNotBlank() && location.isNotBlank() && contract.isNotBlank() && desc.isNotBlank()) {
                    val newId = (FakeRepository.jobOffers.maxOfOrNull { it.id } ?: 0) + 1
                    FakeRepository.jobOffers.add(0, JobOffer(newId, title, desc, location, contract))
                    setupRecyclerView(1)
                    Toast.makeText(requireContext(), "Posizione pubblicata con successo", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Seleziona tutti i campi obbligatori!", Toast.LENGTH_SHORT).show()
                }
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
