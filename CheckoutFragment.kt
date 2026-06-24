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

class CheckoutFragment : Fragment() {

    private var _binding: FragmentCheckoutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCheckoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnConfirmOrder.setOnClickListener {
            // 1. Recuperiamo l'indirizzo dall'EditText (assicurati che nell'XML il TextInputEditText abbia un ID, es: @+id/etAddress)
            val address = binding.tilAddress.editText?.text.toString()

            if (address.isBlank()) {
                // Se l'indirizzo è vuoto, mostriamo errore e blocchiamo
                binding.tilAddress.error = "Inserisci un indirizzo per la consegna"
                Toast.makeText(requireContext(), "Indirizzo mancante!", Toast.LENGTH_SHORT).show()
            } else {
                binding.tilAddress.error = null // Rimuovi errore se presente

                // 2. Chiediamo conferma finale con un Dialog
                com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Conferma Ordine")
                    .setMessage("L'ordine verrà inviato a: $address. Vuoi procedere con il pagamento?")
                    .setNegativeButton("Modifica", null)
                    .setPositiveButton("Paga ora") { _, _ ->
                        // Logica di successo
                        FakeRepository.cart.clear()
                        Toast.makeText(requireContext(), "Pagamento riuscito! Ordine in arrivo.", Toast.LENGTH_LONG).show()
                        findNavController().navigate(R.id.homeFragment)
                    }
                    .show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}