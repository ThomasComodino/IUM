package com.example.superspan.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.superspan.data.Coupon
import com.example.superspan.databinding.ItemAdminCouponBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AdminCouponsAdapter(
    private var list: List<Coupon>,
    private val onToggleClick: (Coupon) -> Unit,
    private val onDeleteClick: (Coupon) -> Unit
) : RecyclerView.Adapter<AdminCouponsAdapter.CouponViewHolder>() {

    inner class CouponViewHolder(val binding: ItemAdminCouponBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponViewHolder {
        val binding = ItemAdminCouponBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CouponViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CouponViewHolder, position: Int) {
        val coupon = list[position]
        with(holder.binding) {
            tvCouponTitle.text = coupon.title
            tvCouponExpiry.text = "Scadenza: ${coupon.expiryDate}"

            // Logica stato (Attivo/Scaduto/In Scadenza)
            val state = getCouponState(coupon)
            tvCouponStatus.text = "STATO: ${state.first}"
            tvCouponStatus.setTextColor(Color.parseColor(state.second))

            btnToggle.text = if (coupon.isActive) "DISATTIVA" else "ATTIVA"
            btnToggle.setOnClickListener { onToggleClick(coupon) }
            btnDelete.setOnClickListener { onDeleteClick(coupon) }
        }
    }

    private fun getCouponState(coupon: Coupon): Pair<String, String> {
        if (!coupon.isActive) return "DISATTIVATO" to "#D32F2F"
        
        return try {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val expiry = sdf.parse(coupon.expiryDate)
            val now = Date()
            
            when {
                expiry == null -> "ATTIVO" to "#2E7D32"
                expiry.before(now) -> "SCADUTO" to "#D32F2F"
                (expiry.time - now.time) < 7 * 24 * 60 * 60 * 1000 -> "IN SCADENZA" to "#FF9800"
                else -> "ATTIVO" to "#2E7D32"
            }
        } catch (e: Exception) {
            "ATTIVO" to "#2E7D32"
        }
    }

    override fun getItemCount() = list.size

    fun updateList(newList: List<Coupon>) {
        list = newList
        notifyDataSetChanged()
    }
}
