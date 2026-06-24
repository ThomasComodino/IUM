package com.example.superspan.ui.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.superspan.R
import com.example.superspan.data.FakeRepository
import com.example.superspan.databinding.FragmentCheckoutBinding

import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient

class CheckoutFragment : Fragment() {

    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!

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
                            if(data.display_name) {
                                // Prendiamo solo la parte principale dell'indirizzo
                                var shortAddress = data.display_name.split(',').slice(0, 2).join(',');
                                Android.onAddressSelected(shortAddress);
                            }
                        });
                });

                function updateMap(address) {
                    fetch('https://nominatim.openstreetmap.org/search?format=json&q=' + encodeURIComponent(address))
                        .then(response => response.json())
                        .then(data => {
                            if(data.length > 0) {
                                var loc = data[0];
                                var latlng = [parseFloat(loc.lat), parseFloat(loc.lon)];
                                map.setView(latlng, 16);
                                marker.setLatLng(latlng);
                            }
                        });
                }
            </script>
        </body>
        </html>
    """.trimIndent()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMap()

        binding.tilAddress.setEndIconOnClickListener {
            val address = binding.etAddress.text.toString()
            if (address.isNotBlank()) {
                binding.webViewMap.evaluateJavascript("updateMap('$address')", null)
            }
        }

        binding.btnConfirmOrder.setOnClickListener {
            val address = binding.etAddress.text.toString()
            if (address.isBlank()) {
                binding.tilAddress.error = "Inserisci un indirizzo per la consegna"
            } else {
                binding.tilAddress.error = null
                showConfirmationDialog(address)
            }
        }
    }

    private fun setupMap() {
        binding.webViewMap.apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            webViewClient = WebViewClient()
            addJavascriptInterface(object {
                @JavascriptInterface
                fun onAddressSelected(address: String) {
                    activity?.runOnUiThread {
                        binding.etAddress.setText(address)
                    }
                }
            }, "Android")
            loadDataWithBaseURL("https://appassets.androidview.com", mapHtml, "text/html", "UTF-8", null)
        }
    }

    private fun showConfirmationDialog(address: String) {
        com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
            .setTitle("Conferma Ordine")
            .setMessage("L'ordine verrà inviato a: $address. Vuoi procedere con il pagamento?")
            .setNegativeButton("Modifica", null)
            .setPositiveButton("Paga ora") { _, _ ->
                FakeRepository.cart.clear()
                Toast.makeText(requireContext(), "Pagamento riuscito!", Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.homeFragment)
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}