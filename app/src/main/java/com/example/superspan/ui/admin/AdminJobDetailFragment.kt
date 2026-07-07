package com.example.superspan.ui.admin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.example.superspan.R
import com.example.superspan.data.FakeRepository
import com.example.superspan.databinding.FragmentAdminJobDetailBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AdminJobDetailFragment : Fragment(R.layout.fragment_admin_job_detail) {
    private var _binding: FragmentAdminJobDetailBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAdminJobDetailBinding.bind(view)

        val name = arguments?.getString("name") ?: ""
        val candidate = FakeRepository.applications.find { it.candidateName == name }

        candidate?.let { c ->
            binding.tvDetailCandidateName.text = c.candidateName
            binding.tvDetailRole.text = c.role
            binding.tvDetailLocation.text = "Sede: ${c.location}"

            binding.btnBackDetail.setOnClickListener {
                parentFragmentManager.popBackStack()
            }

            binding.btnViewCV.setOnClickListener {
                // Ritorno alla prima versione: Apertura tramite URL remoto
                val pdfUrl = "https://www.w3.org/WAI/ER/tests/xhtml/testfiles/resources/pdf/dummy.pdf"
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(pdfUrl.toUri(), "application/pdf")
                intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
                
                try {
                    startActivity(intent)
                } catch (e: Exception) {
                    // Fallback browser se non c'è un lettore PDF dedicato
                    val browserIntent = Intent(Intent.ACTION_VIEW, pdfUrl.toUri())
                    startActivity(browserIntent)
                }
            }

            binding.btnViewVideo.setOnClickListener {
                val videoUrl = "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4"
                val intent = Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(videoUrl.toUri(), "video/mp4")
                
                try {
                    startActivity(intent)
                } catch (e: Exception) {
                    val browserIntent = Intent(Intent.ACTION_VIEW, videoUrl.toUri())
                    startActivity(browserIntent)
                }
            }

            binding.btnForwardHR.setOnClickListener {
                showForwardConfirm("HR Specialist", c.candidateName)
            }

            binding.btnForwardStore.setOnClickListener {
                showForwardConfirm("Responsabile Negozio", c.candidateName)
            }

            binding.btnApprove.setOnClickListener {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Contatta Candidato")
                    .setMessage("Vuoi inviare una mail di convocazione a ${c.candidateName}?")
                    .setNegativeButton("Annulla", null)
                    .setPositiveButton("Invia") { _, _ ->
                        Toast.makeText(requireContext(), "Email di convocazione inviata!", Toast.LENGTH_LONG).show()
                        parentFragmentManager.popBackStack()
                    }.show()
            }
        }
    }

    private fun showForwardConfirm(target: String, name: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Inoltra Candidatura")
            .setMessage("Vuoi inoltrare il profilo di $name a: $target?")
            .setNegativeButton("Annulla", null)
            .setPositiveButton("Inoltra") { _, _ ->
                Toast.makeText(requireContext(), "Candidatura inoltrata con successo a $target", Toast.LENGTH_SHORT).show()
            }.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
