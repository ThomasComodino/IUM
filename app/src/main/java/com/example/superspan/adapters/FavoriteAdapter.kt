package com.example.superspan.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.superspan.data.FavoriteItem
import com.example.superspan.data.FakeRepository
import com.example.superspan.databinding.ItemFavoriteBinding

class FavoriteAdapter(
    private val list: List<FavoriteItem>,
    private val onItemClick: (Int) -> Unit // Riceve un Intero (l'ID del prodotto)
) : RecyclerView.Adapter<FavoriteAdapter.FavViewHolder>() {

    inner class FavViewHolder(val binding: ItemFavoriteBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
        val b = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavViewHolder(b)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        val item = list[position]
        val currentPrice = FakeRepository.getFinalPrice(item.product)

        with(holder.binding) {
            tvFavName.text = item.product.name
            tvSavedPrice.text = "Al salvataggio: € ${String.format("%.2f", item.savedPrice)}"
            tvCurrentPrice.text = "Prezzo oggi: € ${String.format("%.2f", currentPrice)}"

            // Logica colori (già vista)
            if (currentPrice < item.savedPrice) {
                ivPriceIndicator.setColorFilter(Color.parseColor("#2E7D32")) // Verde
            } else if (currentPrice > item.savedPrice) {
                ivPriceIndicator.setColorFilter(Color.RED)
            } else {
                ivPriceIndicator.setColorFilter(Color.LTGRAY)
            }

            // CLICK SULLA CARD: Passiamo l'ID reale del prodotto
            root.setOnClickListener {
                onItemClick(item.product.id)
            }
        }
    }

    override fun getItemCount() = list.size
}