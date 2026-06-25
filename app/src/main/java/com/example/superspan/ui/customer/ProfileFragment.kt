package com.example.superspan.ui.customer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.superspan.R
import com.example.superspan.adapters.FavoriteAdapter
import com.example.superspan.data.FakeRepository
import com.example.superspan.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Carichiamo i punti
        binding.tvPoints.text = "1.250 Punti"

        // Setup della lista
        val favoritesList = FakeRepository.favorites
        if (favoritesList.isEmpty()) {
            binding.rvFavorites.visibility = View.GONE
            binding.tvNoFavorites.visibility = View.VISIBLE
        } else {
            binding.rvFavorites.visibility = View.VISIBLE
            binding.tvNoFavorites.visibility = View.GONE
            binding.rvFavorites.layoutManager = LinearLayoutManager(requireContext())
            binding.rvFavorites.adapter = FavoriteAdapter(favoritesList) { productId ->
                val bundle = Bundle().apply { putInt("productId", productId) }
                findNavController().navigate(R.id.productDetailFragment, bundle)
            }
        }

        // Nuovi listener per le card di Claudia
        binding.cardOrders.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_ordersFragment)
        }
        binding.cardAddresses.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_addressesFragment)
        }

        // Logout
        binding.btnLogout.setOnClickListener {
            // Pulizia selettiva delle SharedPreferences
            val pref = requireContext().getSharedPreferences("SUperSpanPrefs", Context.MODE_PRIVATE)
            val savedEmail = pref.getString("email", "")
            val savedPass = pref.getString("pass", "")

            val editor = pref.edit()
            editor.clear() // Puliamo tutto per sicurezza...
            
            // ...ma se c'erano dati salvati con "Ricordami", li ripristiniamo subito
            if (!savedEmail.isNullOrBlank()) {
                editor.putString("email", savedEmail)
                editor.putString("pass", savedPass)
            }
            editor.apply()

            val intent = Intent(requireContext(), com.example.superspan.ui.auth.LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}