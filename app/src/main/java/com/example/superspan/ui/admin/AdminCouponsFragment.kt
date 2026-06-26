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
import com.example.superspan.adapters.AdminCouponsAdapter
import com.example.superspan.data.FakeRepository
import com.example.superspan.data.Coupon
import com.example.superspan.databinding.DialogAddCouponBinding
import com.example.superspan.databinding.FragmentAdminCouponsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AdminCouponsFragment : Fragment() {
    private var _binding: FragmentAdminCouponsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAdminCouponsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = AdminCouponsAdapter(
            FakeRepository.adminCoupons,
            onToggleClick = { coupon ->
                val currentlyActive = coupon.isActive
                val action = if (currentlyActive) "disattivare" else "attivare"
                
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Conferma")
                    .setMessage("Vuoi davvero $action il coupon '${coupon.title}'?")
                    .setNegativeButton("Annulla", null)
                    .setPositiveButton("Conferma") { _, _ ->
                        coupon.isActive = !currentlyActive
                        updateUI()
                        Toast.makeText(requireContext(), "Coupon aggiornato", Toast.LENGTH_SHORT).show()
                    }.show()
            },
            onDeleteClick = { coupon ->
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Elimina Coupon")
                    .setMessage("Vuoi eliminare definitivamente questo coupon?")
                    .setNegativeButton("Annulla", null)
                    .setPositiveButton("Elimina") { _, _ ->
                        FakeRepository.adminCoupons.remove(coupon)
                        updateUI()
                        Toast.makeText(requireContext(), "Coupon eliminato", Toast.LENGTH_SHORT).show()
                    }.show()
            }
        )

        binding.rvAdminCoupons.layoutManager = LinearLayoutManager(requireContext())
        binding.rvAdminCoupons.adapter = adapter

        // Aggiungiamo lo scorrimento se necessario
        binding.rvAdminCoupons.isNestedScrollingEnabled = false

        binding.fabAddCoupon.setOnClickListener {
            showAddCouponDialog()
        }
    }

    private fun showAddCouponDialog() {
        val dialogBinding = DialogAddCouponBinding.inflate(layoutInflater)
        val productsNames = FakeRepository.products.map { it.name }.toTypedArray()
        val selectedItems = mutableListOf<Int>()

        // Setup Dropdown Categorie
        val categories = listOf("Alimentari", "Bevande", "Dolci", "Casalinghi")
        val catAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, categories)
        dialogBinding.autoCompleteCategory.setAdapter(catAdapter)

        // Logica visibilità condizionale
        dialogBinding.rgCouponUsage.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbOnline) {
                dialogBinding.rgStoreType.visibility = View.GONE
                dialogBinding.layoutDiscountFields.visibility = View.VISIBLE
                dialogBinding.layoutGiftFields.visibility = View.GONE
                dialogBinding.tilCouponTitle.visibility = View.GONE
            } else {
                dialogBinding.rgStoreType.visibility = View.VISIBLE
                // Trigger logica sottogruppo
                dialogBinding.rgStoreType.check(R.id.rbStoreDiscount)
            }
        }

        dialogBinding.rgStoreType.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbStoreDiscount) {
                dialogBinding.layoutDiscountFields.visibility = View.VISIBLE
                dialogBinding.layoutGiftFields.visibility = View.GONE
                dialogBinding.tilCouponTitle.visibility = View.GONE
            } else {
                dialogBinding.layoutDiscountFields.visibility = View.GONE
                dialogBinding.layoutGiftFields.visibility = View.VISIBLE
                dialogBinding.tilCouponTitle.visibility = View.VISIBLE
            }
        }

        // Inizializzazione corretta
        if (dialogBinding.rbOnline.isChecked || dialogBinding.rbStoreDiscount.isChecked) {
            dialogBinding.tilCouponTitle.visibility = View.GONE
        }

        dialogBinding.btnSelectProducts.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Seleziona 3 Prodotti")
                .setMultiChoiceItems(productsNames, null) { _, which, isChecked ->
                    if (isChecked) selectedItems.add(which) else selectedItems.remove(which)
                }
                .setPositiveButton("OK") { _, _ ->
                    dialogBinding.tvSelectedCount.text = "Prodotti selezionati: ${selectedItems.size}"
                }
                .show()
        }

        // Setup DatePicker
        dialogBinding.etCouponExpiry.setOnClickListener {
            val calendar = java.util.Calendar.getInstance()
            val datePicker = android.app.DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    val selectedDate = "${String.format("%02d", dayOfMonth)}/${String.format("%02d", month + 1)}/$year"
                    dialogBinding.etCouponExpiry.setText(selectedDate)
                },
                calendar.get(java.util.Calendar.YEAR),
                calendar.get(java.util.Calendar.MONTH),
                calendar.get(java.util.Calendar.DAY_OF_MONTH)
            )
            datePicker.datePicker.minDate = System.currentTimeMillis() - 1000
            datePicker.show()
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Nuovo Coupon")
            .setView(dialogBinding.root)
            .setNegativeButton("Annulla", null)
            .setPositiveButton("Crea") { _, _ ->
                val date = dialogBinding.etCouponExpiry.text.toString()
                val isOnline = dialogBinding.rbOnline.isChecked
                
                if (date.isBlank()) {
                    Toast.makeText(requireContext(), "Scadenza obbligatoria!", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val newId = (FakeRepository.adminCoupons.maxOfOrNull { it.id } ?: 0) + 1
                
                if (isOnline || dialogBinding.rbStoreDiscount.isChecked) {
                    val discount = dialogBinding.etDiscount.text.toString().toIntOrNull() ?: 0
                    val category = dialogBinding.autoCompleteCategory.text.toString()
                    
                    if (discount > 0 && category.isNotBlank()) {
                        val autoTitle = "Sconto del $discount% su $category"
                        FakeRepository.adminCoupons.add(0, Coupon(newId, autoTitle, date, true, isOnline, "SCONTO", category, discount))
                        updateUI()
                        Toast.makeText(requireContext(), "Coupon '$autoTitle' creato!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Inserisci sconto e categoria!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // STORE GIFT
                    val title = dialogBinding.etCouponTitle.text.toString()
                    if (title.isBlank()) {
                        Toast.makeText(requireContext(), "Titolo obbligatorio per i regali!", Toast.LENGTH_SHORT).show()
                        return@setPositiveButton
                    }

                    if (selectedItems.size == 3) {
                        val productIds = selectedItems.map { FakeRepository.products[it].id }
                        FakeRepository.adminCoupons.add(0, Coupon(newId, title, date, true, false, "GIFT", productIds = productIds))
                        updateUI()
                        Toast.makeText(requireContext(), "Coupon regalo creato!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Seleziona esattamente 3 prodotti!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .show()
    }

    private fun updateUI() {
        (binding.rvAdminCoupons.adapter as AdminCouponsAdapter).updateList(FakeRepository.adminCoupons)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
