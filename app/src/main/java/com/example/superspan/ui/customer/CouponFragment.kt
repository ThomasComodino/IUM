package com.example.superspan.ui.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.superspan.adapters.ClientCouponsAdapter
import com.example.superspan.data.FakeRepository
import com.example.superspan.databinding.FragmentCouponBinding

class CouponFragment : Fragment() {
    private var _binding: FragmentCouponBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCouponBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activeCoupons = FakeRepository.adminCoupons.filter { coupon ->
            if (!coupon.isActive) return@filter false
            
            // Filtro per data di scadenza
            try {
                val sdf = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault())
                val expiryDate = sdf.parse(coupon.expiryDate)
                val now = java.util.Date()
                expiryDate != null && !expiryDate.before(sdf.parse(sdf.format(now)))
            } catch (e: Exception) {
                true // In caso di errore lo mostriamo comunque
            }
        }

        if (activeCoupons.isEmpty()) {
            binding.rvCoupons.visibility = View.GONE
            binding.tvNoCoupons.visibility = View.VISIBLE
        } else {
            binding.rvCoupons.visibility = View.VISIBLE
            binding.tvNoCoupons.visibility = View.GONE
            binding.rvCoupons.layoutManager = LinearLayoutManager(requireContext())
            binding.rvCoupons.adapter = ClientCouponsAdapter(activeCoupons) {
                // Callback dopo attivazione (opzionale se vogliamo rinfrescare)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
