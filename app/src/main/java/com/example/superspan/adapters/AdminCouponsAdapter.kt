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
            val color = if (coupon.isActive) "#D32F2F" else "#2E7D32"
            btnToggle.setBackgroundColor(Color.parseColor(color))

            btnToggle.setOnClickListener { onToggleClick(coupon) }
            btnDelete.setOnClickListener { onDeleteClick(coupon) }
        }
    }

    private fun getCouponState(coupon: Coupon): Pair<String, String> {
        try {
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val expiry = sdf.parse(coupon.expiryDate)
            val now = sdf.parse(sdf.format(Date()))
            
            // 1. La scadenza ha la priorità assoluta
            if (expiry != null && expiry.before(now)) {
                return "SCADUTO" to "#D32F2F"
            }
            
            // 2. Se non è scaduto, controlliamo lo stato manuale
            if (!coupon.isActive) return "DISATTIVATO" to "#D32F2F"

            // 3. Se è attivo, verifichiamo se è in scadenza (prossimi 7 giorni)
            return when {
                expiry == null -> "ATTIVO" to "#2E7D32"
                (expiry.time - Date().time) < 7 * 24 * 60 * 60 * 1000 -> "IN SCADENZA" to "#FF9800"
                else -> "ATTIVO" to "#2E7D32"
            }
        } catch (e: Exception) {
            return if (coupon.isActive) "ATTIVO" to "#2E7D32" else "DISATTIVATO" to "#D32F2F"
        }
    }

    override fun getItemCount() = list.size

    fun updateList(newList: List<Coupon>) {
        list = newList
        notifyDataSetChanged()
    }
}
