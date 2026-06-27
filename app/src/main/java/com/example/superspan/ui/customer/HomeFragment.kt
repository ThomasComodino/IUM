package com.example.superspan.ui.customer

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.superspan.adapters.ProductAdapter
import com.example.superspan.data.FakeRepository
import com.example.superspan.databinding.FragmentHomeBinding
import androidx.navigation.fragment.findNavController
import com.example.superspan.R

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var currentSearchText = ""
    private var currentCategory = "Tutti"
    private var onlyOffers = false
    private var isAscending = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ProductAdapter(
            products = FakeRepository.products,
            onProductClick = { product ->
                val bundle = Bundle().apply { putInt("productId", product.id) }
                findNavController().navigate(R.id.productDetailFragment, bundle)
            },
            onAddClick = { product ->
                FakeRepository.addToCart(product)
                Toast.makeText(requireContext(), "${product.name} aggiunto!", Toast.LENGTH_SHORT).show()
            }
        )

        binding.rvProducts.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvProducts.adapter = adapter

        updatePromoBanners()

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                currentSearchText = s.toString()
                applyFilters()
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        binding.chipGroupFilters.setOnCheckedStateChangeListener { _, checkedIds ->
            val chipId = checkedIds.firstOrNull()
            onlyOffers = (chipId == R.id.chipOffers)
            currentCategory = when (chipId) {
                R.id.chipAlimentari -> "Alimentari"
                R.id.chipBevande -> "Bevande"
                else -> "Tutti"
            }
            applyFilters()
        }

        binding.btnSort.setOnClickListener {
            isAscending = !isAscending
            applyFilters()
        }
    }

    private fun updatePromoBanners() {
        binding.layoutPromoContainer.removeAllViews()
        
        val activePromos = FakeRepository.promotions.filter { promo ->
            if (!promo.isActive) return@filter false
            try {
                val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                val expiry = sdf.parse(promo.validUntil)
                val now = sdf.parse(sdf.format(java.util.Date()))
                expiry != null && !expiry.before(now)
            } catch (e: Exception) {
                true
            }
        }

        activePromos.forEach { promo ->
            val banner = com.google.android.material.card.MaterialCardView(requireContext()).apply {
                val lp = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                lp.setMargins(48, 16, 48, 8)
                layoutParams = lp
                radius = 32f
                setCardBackgroundColor(resources.getColor(R.color.orange_accent))
                
                val rootLayout = LinearLayout(context).apply {
                    orientation = LinearLayout.HORIZONTAL
                    setPadding(48, 32, 48, 32)
                    gravity = android.view.Gravity.CENTER_VERTICAL
                }
                
                val icon = android.widget.ImageView(context).apply {
                    layoutParams = LinearLayout.LayoutParams(80, 80)
                    setImageResource(android.R.drawable.ic_menu_send)
                    setColorFilter(android.graphics.Color.WHITE)
                }
                
                val text = android.widget.TextView(context).apply {
                    val tlp = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    tlp.setMargins(32, 0, 0, 0)
                    layoutParams = tlp
                    text = "OFFERTA: ${promo.title}!"
                    setTextColor(android.graphics.Color.WHITE)
                    setTypeface(null, android.graphics.Typeface.BOLD)
                    textSize = 14f
                }
                
                rootLayout.addView(icon)
                rootLayout.addView(text)
                addView(rootLayout)
            }
            binding.layoutPromoContainer.addView(banner)
        }
    }

    private fun applyFilters() {
        var list = FakeRepository.products

        if (currentSearchText.isNotEmpty()) {
            list = list.filter { it.name.contains(currentSearchText, ignoreCase = true) }
        }

        if (currentCategory != "Tutti") {
            list = list.filter { it.category == currentCategory }
        }

        if (onlyOffers) {
            list = list.filter { FakeRepository.getFinalPrice(it) < it.price }
        }

        list = if (isAscending) {
            list.sortedBy { FakeRepository.getFinalPrice(it) }
        } else {
            list.sortedByDescending { FakeRepository.getFinalPrice(it) }
        }

        (binding.rvProducts.adapter as ProductAdapter).updateList(list)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
