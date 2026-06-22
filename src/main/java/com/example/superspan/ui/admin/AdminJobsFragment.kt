package com.example.superspan.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.superspan.data.FakeRepository
import com.example.superspan.databinding.FragmentAdminJobsBinding

/**
 * Schermata Candidature (Area Risorse Umane per Daniela).
 * Visualizza la lista dei candidati e i relativi Video CV.
 */
class AdminJobsFragment : Fragment() {

    private var _binding: FragmentAdminJobsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminJobsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Caricamento dei dati dal FakeRepository e setup della RecyclerView
        val adapter = ApplicationAdapter(FakeRepository.applications)
        binding.rvApplications.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
