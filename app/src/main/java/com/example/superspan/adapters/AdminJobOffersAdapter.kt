package com.example.superspan.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.superspan.data.JobOffer
import com.example.superspan.databinding.ItemJobOfferBinding

class AdminJobOffersAdapter(
    private var list: List<JobOffer>
) : RecyclerView.Adapter<AdminJobOffersAdapter.OfferViewHolder>() {

    inner class OfferViewHolder(val binding: ItemJobOfferBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {
        val binding = ItemJobOfferBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OfferViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        val offer = list[position]
        with(holder.binding) {
            tvJobTitle.text = offer.title
            tvJobDetails.text = "Sede: ${offer.location} | Contratto: ${offer.contractType}"
            tvJobDescription.text = offer.description
        }
    }

    override fun getItemCount() = list.size

    fun updateList(newList: List<JobOffer>) {
        list = newList
        notifyDataSetChanged()
    }
}
