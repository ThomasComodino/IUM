package com.example.superspan.ui.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.superspan.R
import com.example.superspan.data.Address
import com.example.superspan.data.FakeRepository
import com.google.android.material.textfield.TextInputEditText

class AddressesFragment : Fragment() {

    private lateinit var adapter: AddressesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_addresses, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val rvAddresses = view.findViewById<RecyclerView>(R.id.rvAddresses)
        val tvNoAddresses = view.findViewById<TextView>(R.id.tvNoAddresses)

        adapter = AddressesAdapter(FakeRepository.addresses, 
            onDeleted = {
                updateEmptyState(rvAddresses, tvNoAddresses)
            },
            onEdit = { address ->
                showAddressDialog(address)
            }
        )

        rvAddresses.layoutManager = LinearLayoutManager(requireContext())
        rvAddresses.adapter = adapter
        updateEmptyState(rvAddresses, tvNoAddresses)

        val fabAddAddress = view.findViewById<View>(R.id.fabAddAddress)
        fabAddAddress.setOnClickListener {
            showAddressDialog(null)
        }
    }

    private fun updateEmptyState(rv: RecyclerView, tv: TextView) {
        if (FakeRepository.addresses.isEmpty()) {
            rv.visibility = View.GONE
            tv.visibility = View.VISIBLE
        } else {
            rv.visibility = View.VISIBLE
            tv.visibility = View.GONE
        }
    }

    private fun showAddressDialog(address: Address?) {
        val builder = AlertDialog.Builder(requireContext())
        val dialogView = layoutInflater.inflate(R.layout.dialog_address, null)
        builder.setView(dialogView)

        val tvTitle = dialogView.findViewById<TextView>(R.id.tvDialogTitle)
        val etName = dialogView.findViewById<TextInputEditText>(R.id.etAddressName)
        val etFull = dialogView.findViewById<TextInputEditText>(R.id.etFullAddress)

        if (address != null) {
            tvTitle.text = "Modifica Indirizzo"
            etName.setText(address.name)
            etFull.setText(address.fullAddress)
        }

        builder.setPositiveButton("Salva") { dialog, _ ->
            val name = etName.text.toString()
            val full = etFull.text.toString()

            if (name.isNotBlank() && full.isNotBlank()) {
                if (address == null) {
                    // Aggiunta
                    val newId = (FakeRepository.addresses.maxOfOrNull { it.id } ?: 0) + 1
                    FakeRepository.addresses.add(Address(newId, name, full))
                    adapter.notifyItemInserted(FakeRepository.addresses.size - 1)
                } else {
                    // Modifica
                    val index = FakeRepository.addresses.indexOf(address)
                    if (index != -1) {
                        FakeRepository.addresses[index] = address.copy(name = name, fullAddress = full)
                        adapter.notifyItemChanged(index)
                    }
                }
                view?.let { updateEmptyState(it.findViewById(R.id.rvAddresses), it.findViewById(R.id.tvNoAddresses)) }
            } else {
                Toast.makeText(requireContext(), "Compila tutti i campi", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("Annulla") { dialog, _ -> dialog.dismiss() }
        builder.show()
    }

    class AddressesAdapter(
        private val addresses: MutableList<Address>,
        private val onDeleted: () -> Unit,
        private val onEdit: (Address) -> Unit
    ) : RecyclerView.Adapter<AddressesAdapter.AddressViewHolder>() {
        
        class AddressViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvName: TextView = view.findViewById(R.id.tvAddressName)
            val tvFull: TextView = view.findViewById(R.id.tvFullAddress)
            val btnEdit: View = view.findViewById(R.id.btnEditAddress)
            val btnDelete: View = view.findViewById(R.id.btnDeleteAddress)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_address, parent, false)
            return AddressViewHolder(view)
        }

        override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
            val address = addresses[position]
            holder.tvName.text = address.name
            holder.tvFull.text = address.fullAddress

            holder.btnEdit.setOnClickListener {
                onEdit(address)
            }

            holder.btnDelete.setOnClickListener {
                val currentPos = holder.bindingAdapterPosition
                if (currentPos != RecyclerView.NO_POSITION) {
                    addresses.removeAt(currentPos)
                    notifyItemRemoved(currentPos)
                    onDeleted()
                }
            }
        }

        override fun getItemCount() = addresses.size
    }
}