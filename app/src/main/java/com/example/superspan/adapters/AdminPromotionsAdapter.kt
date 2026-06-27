package com.example.superspan.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.superspan.data.Promotion
import com.example.superspan.databinding.ItemPromotionBinding
import com.example.superspan.R

class AdminPromotionsAdapter(
    private var list: List<Promotion>,
    private val onToggleClick: (Promotion) -> Unit,
    private val onDeleteClick: (Promotion) -> Unit
) : RecyclerView.Adapter<AdminPromotionsAdapter.PromoViewHolder>() {

    inner class PromoViewHolder(val binding: ItemPromotionBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromoViewHolder {
        val binding = ItemPromotionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PromoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PromoViewHolder, position: Int) {
        val promo = list[position]
        with(holder.binding) {
            tvPromoTitle.text = promo.title
            tvPromoDetails.text = "Categoria: ${promo.category} | Sconto: ${promo.discountPercent}%"
            tvPromoValidity.text = "Valida fino al: ${promo.validUntil}"
            
            if (promo.isActive) {
                tvPromoStatus.text = "STATO: ATTIVA"
                tvPromoStatus.setTextColor(root.context.getColor(R.color.green_super))
                btnTogglePromo.text = "DISATTIVA"
                btnTogglePromo.setBackgroundColor(root.context.getColor(R.color.red_error))
            } else {
                tvPromoStatus.text = "STATO: DISATTIVATA"
                tvPromoStatus.setTextColor(root.context.getColor(R.color.red_error))
                btnTogglePromo.text = "ATTIVA"
                btnTogglePromo.setBackgroundColor(root.context.getColor(R.color.green_super))
            }

            btnTogglePromo.setOnClickListener { onToggleClick(promo) }
            btnDeletePromo.setOnClickListener { onDeleteClick(promo) }
        }
    }

    override fun getItemCount() = list.size

    fun updateList(newList: List<Promotion>) {
        list = newList
        notifyDataSetChanged()
    }
}
