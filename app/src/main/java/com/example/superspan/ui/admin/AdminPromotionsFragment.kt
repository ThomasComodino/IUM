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

        val adapter = AdminPromotionsAdapter(
            FakeRepository.promotions,
            onToggleClick = { promo ->
                val currentlyActive = promo.isActive
                val action = if (currentlyActive) "disattivare" else "attivare"
                
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Conferma")
                    .setMessage("Vuoi davvero $action l'offerta '${promo.title}'?")
                    .setNegativeButton("Annulla", null)
                    .setPositiveButton("Conferma") { _, _ ->
                        promo.isActive = !currentlyActive
                        updateList()
                        Toast.makeText(requireContext(), "Offerta aggiornata", Toast.LENGTH_SHORT).show()
                    }.show()
            },
            onDeleteClick = { promo ->
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
        )

        binding.rvPromotions.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPromotions.adapter = adapter

        binding.fabAddPromotion.setOnClickListener {
            showAddPromotionDialog()
        }
    }

    private fun showAddPromotionDialog() {
        val dialogBinding = DialogAddPromotionBinding.inflate(layoutInflater)
        
        val categories = listOf("Alimentari", "Bevande", "Dolci", "Casalinghi")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, categories)
        dialogBinding.autoCompleteCategory.setAdapter(adapter)

        dialogBinding.etValidUntil.setOnClickListener {
            val calendar = java.util.Calendar.getInstance()
            val datePicker = android.app.DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    val selectedDate = "${String.format("%02d", dayOfMonth)}/${String.format("%02d", month + 1)}/$year"
                    dialogBinding.etValidUntil.setText(selectedDate)
                },
                calendar.get(java.util.Calendar.YEAR),
                calendar.get(java.util.Calendar.MONTH),
                calendar.get(java.util.Calendar.DAY_OF_MONTH)
            )
            datePicker.datePicker.minDate = System.currentTimeMillis() - 1000
            datePicker.show()
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Nuova Offerta")
            .setView(dialogBinding.root)
            .setNegativeButton("Annulla", null)
            .setPositiveButton("Crea") { _, _ ->
                val category = dialogBinding.autoCompleteCategory.text.toString()
                val discount = dialogBinding.etDiscount.text.toString().toIntOrNull() ?: 0
                val date = dialogBinding.etValidUntil.text.toString()

                if (category.isNotBlank() && discount > 0 && date.isNotBlank()) {
                    val title = "Sconto del $discount% su $category"
                    val newId = (FakeRepository.promotions.maxOfOrNull { it.id } ?: 0) + 1
                    // Inizialmente disattivata
                    FakeRepository.promotions.add(0, Promotion(newId, title, category, discount, date, false))
                    updateList()
                    Toast.makeText(requireContext(), "Offerta creata (disattivata): $title", Toast.LENGTH_SHORT).show()
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
