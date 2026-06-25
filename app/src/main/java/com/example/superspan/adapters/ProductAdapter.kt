package com.example.superspan.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.superspan.data.Product
import com.example.superspan.data.FakeRepository // IMPORTANTE: per calcolare il prezzo dinamico
import com.example.superspan.databinding.ItemProductBinding

class ProductAdapter(
    private var products: List<Product>,
    private val onProductClick: (Product) -> Unit,
    private val onAddClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    fun updateList(newList: List<Product>) {
        this.products = newList
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        with(holder.binding) {
            tvName.text = product.name
            
            // Chiediamo il prezzo finale aggiornato al repository
            val finalPrice = FakeRepository.getFinalPrice(product)
            tvPrice.text = "€ " + String.format("%.2f", finalPrice)

            // --- LOGICA PREZZO SBARRATO NELLA HOME ---
            if (finalPrice < product.price) {
                // Se c'è uno sconto attivo (es. coupon di Michele)
                tvOldPriceHome.visibility = android.view.View.VISIBLE
                tvOldPriceHome.text = "€ " + String.format("%.2f", product.price)
                // Applichiamo la sbarra sopra il testo
                tvOldPriceHome.paintFlags = tvOldPriceHome.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                // Altrimenti nascondiamo il prezzo vecchio
                tvOldPriceHome.visibility = android.view.View.GONE
            }

            // Navigazione e aggiunta rapida
            root.setOnClickListener { onProductClick(product) }
            btnAdd.setOnClickListener { onAddClick(product) }
        }
    }

    override fun getItemCount() = products.size

}