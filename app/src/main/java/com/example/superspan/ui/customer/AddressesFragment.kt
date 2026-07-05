package com.example.superspan.ui.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebViewClient
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.superspan.R
import com.example.superspan.data.Address
import com.example.superspan.data.FakeRepository
import com.example.superspan.databinding.DialogAddAddressBinding
import com.example.superspan.databinding.FragmentAddressesBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class AddressesFragment : Fragment() {

    private var _binding: FragmentAddressesBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: AddressesAdapter

    private val mapHtml = """
        <!DOCTYPE html>
        <html>
        <head>
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />
            <script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>
            <style>
                #map { height: 100vh; width: 100%; margin: 0; padding: 0; }
                .leaflet-control-attribution { display: none; }
            </style>
        </head>
        <body style="margin: 0;">
            <div id="map"></div>
            <script>
                var map = L.map('map').setView([45.4642, 9.1900], 13);
                L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(map);
                var marker = L.marker([45.4642, 9.1900], {draggable: true}).addTo(map);

                marker.on('dragend', function(e) {
                    var latlng = marker.getLatLng();
                    fetch('https://nominatim.openstreetmap.org/reverse?format=json&lat=' + latlng.lat + '&lon=' + latlng.lng)
                        .then(response => response.json())
                        .then(data => {
                            if(data.address) {
                                var road = data.address.road || "";
                                var houseNumber = data.address.house_number || "";
                                var city = data.address.city || data.address.town || data.address.village || "";
                                var streetAddress = road + (houseNumber ? " " + houseNumber : "");
                                Android.onAddressSelected(streetAddress, city);
                            }
                        });
                });
            </script>
        </body>
        </html>
    """.trimIndent()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddressesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = AddressesAdapter(FakeRepository.addresses, 
            onDeleted = { updateEmptyState() },
            onEdit = { showAddressDialog(it) }
        )

        binding.rvAddresses.layoutManager = LinearLayoutManager(requireContext())
        binding.rvAddresses.adapter = adapter

        updateEmptyState()

        binding.fabAddAddress.setOnClickListener {
            showAddressDialog(null)
        }
    }

    private fun updateEmptyState() {
        if (FakeRepository.addresses.isEmpty()) {
            binding.rvAddresses.visibility = View.GONE
            binding.tvNoAddresses.visibility = View.VISIBLE
        } else {
            binding.rvAddresses.visibility = View.VISIBLE
            binding.tvNoAddresses.visibility = View.GONE
        }
    }

    private fun showAddressDialog(existing: Address?) {
        val dialogBinding = DialogAddAddressBinding.inflate(layoutInflater)
        
        // Se stiamo modificando, precompiliamo
        existing?.let {
            dialogBinding.etDialogAddrName.setText(it.name)
            // Estrapoliamo via e città (presumiamo formato "Via ..., Città")
            val parts = it.fullAddress.split(", ")
            dialogBinding.etDialogAddrStreet.setText(parts.getOrNull(0) ?: it.fullAddress)
            dialogBinding.etDialogAddrCity.setText(parts.getOrNull(1) ?: "")
        }

        // Setup Mappa nel dialogo
        dialogBinding.wvDialogMap.apply {
            settings.javaScriptEnabled = true
            webViewClient = android.webkit.WebViewClient()
            addJavascriptInterface(object {
                @JavascriptInterface
                fun onAddressSelected(address: String, city: String) {
                    activity?.runOnUiThread {
                        dialogBinding.etDialogAddrStreet.setText(address)
                        dialogBinding.etDialogAddrCity.setText(city)
                    }
                }
            }, "Android")
            loadDataWithBaseURL("https://appassets.androidview.com", mapHtml, "text/html", "UTF-8", null)
        }

        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setTitle(if (existing == null) "Aggiungi Indirizzo" else "Modifica Indirizzo")
            .setView(dialogBinding.root)
            .setNegativeButton("Annulla", null)
            .setPositiveButton("Salva", null) // Lo sovrascriviamo sotto per la validazione
            .show()

        dialog.getButton(android.content.DialogInterface.BUTTON_POSITIVE).setOnClickListener {
            val name = dialogBinding.etDialogAddrName.text.toString()
            val street = dialogBinding.etDialogAddrStreet.text.toString()
            val city = dialogBinding.etDialogAddrCity.text.toString()

            var isValid = true
            if (name.isBlank()) {
                dialogBinding.tilDialogAddrName.error = "Nome obbligatorio"
                isValid = false
            } else dialogBinding.tilDialogAddrName.error = null

            if (street.isBlank()) {
                dialogBinding.tilDialogAddrStreet.error = "Indirizzo obbligatorio"
                isValid = false
            } else dialogBinding.tilDialogAddrStreet.error = null

            if (city.isBlank()) {
                dialogBinding.tilDialogAddrCity.error = "Città obbligatoria"
                isValid = false
            } else dialogBinding.tilDialogAddrCity.error = null

            if (isValid) {
                val fullAddr = "$street, $city"
                if (existing == null) {
                    val newId = (FakeRepository.addresses.maxOfOrNull { it.id } ?: 0) + 1
                    FakeRepository.addresses.add(Address(newId, name, fullAddr))
                } else {
                    val index = FakeRepository.addresses.indexOfFirst { it.id == existing.id }
                    if (index != -1) {
                        FakeRepository.addresses[index] = Address(existing.id, name, fullAddr)
                    }
                }
                adapter.notifyDataSetChanged()
                updateEmptyState()
                dialog.dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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

            holder.btnEdit.setOnClickListener { onEdit(address) }

            holder.btnDelete.setOnClickListener {
                MaterialAlertDialogBuilder(holder.itemView.context)
                    .setTitle("Elimina Indirizzo")
                    .setMessage("Vuoi davvero rimuovere '${address.name}'?")
                    .setNegativeButton("Annulla", null)
                    .setPositiveButton("Elimina") { _, _ ->
                        addresses.removeAt(position)
                        notifyItemRemoved(position)
                        onDeleted()
                    }.show()
            }
        }

        override fun getItemCount() = addresses.size
    }
}
