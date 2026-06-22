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

            // --- CAMBIAMENTO QUI: Chiediamo il prezzo finale aggiornato ---
            val finalPrice = FakeRepository.getFinalPrice(product)
            tvPrice.text = "€ " + String.format("%.2f", finalPrice)

            // Click sulla card per il dettaglio
            root.setOnClickListener { onProductClick(product) }

            // Click sul tasto "+" rapido
            btnAdd.setOnClickListener { onAddClick(product) }
        }
    }

    override fun getItemCount() = products.size

}