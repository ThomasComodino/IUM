package com.example.superspan.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.superspan.R
import com.example.superspan.adapters.AdminPromotionsAdapter
import com.example.superspan.data.FakeRepository
import com.example.superspan.data.Promotion
import com.example.superspan.databinding.DialogAddPromotionBinding
import com.example.superspan.databinding.FragmentAdminPromotionsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AdminPromotionsFragment : Fragment() {
    private var _binding: FragmentAdminPromotionsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAdminPromotionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = AdminPromotionsAdapter(FakeRepository.promotions) { promo ->
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Rimuovi Offerta")
                .setMessage("Sei sicuro di voler rimuovere l'offerta '${promo.title}'?")
                .setNegativeButton("Annulla", null)
                .setPositiveButton("Rimuovi") { _, _ ->
                    FakeRepository.promotions.remove(promo)
                    updateList()
                    Toast.makeText(requireContext(), "Promozione rimossa con successo", Toast.LENGTH_SHORT).show()
                }
                .show()
        }

        binding.rvPromotions.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPromotions.adapter = adapter

        binding.fabAddPromotion.setOnClickListener {
            showAddPromotionDialog()
        }
    }

    private fun showAddPromotionDialog() {
        val dialogBinding = DialogAddPromotionBinding.inflate(layoutInflater)
        
        // Setup dropdown categorie
        val categories = listOf("Alimentari", "Bevande", "Dolci", "Casalinghi")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, categories)
        dialogBinding.autoCompleteCategory.setAdapter(adapter)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Nuova Promozione")
            .setView(dialogBinding.root)
            .setNegativeButton("Annulla", null)
            .setPositiveButton("Pubblica") { _, _ ->
                val title = dialogBinding.etPromoTitle.text.toString()
                val category = dialogBinding.autoCompleteCategory.text.toString()
                val discount = dialogBinding.etDiscount.text.toString().toIntOrNull() ?: 0
                val date = dialogBinding.etValidUntil.text.toString()

                if (title.isNotBlank() && category.isNotBlank() && discount > 0 && date.isNotBlank()) {
                    val newId = (FakeRepository.promotions.maxOfOrNull { it.id } ?: 0) + 1
                    FakeRepository.promotions.add(Promotion(newId, title, category, discount, date))
                    updateList()
                    Toast.makeText(requireContext(), "Promozione pubblicata con successo", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Compila tutti i campi correttamente!", Toast.LENGTH_SHORT).show()
                }
            }
            .show()
    }

    private fun updateList() {
        (binding.rvPromotions.adapter as AdminPromotionsAdapter).updateList(FakeRepository.promotions)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
