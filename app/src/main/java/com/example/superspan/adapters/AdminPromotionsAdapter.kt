package com.example.superspan.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.superspan.data.Promotion
import com.example.superspan.databinding.ItemPromotionBinding

class AdminPromotionsAdapter(
    private var list: List<Promotion>,
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
            btnDeletePromo.setOnClickListener { onDeleteClick(promo) }
        }
    }

    override fun getItemCount() = list.size

    fun updateList(newList: List<Promotion>) {
        list = newList
        notifyDataSetChanged()
    }
}
