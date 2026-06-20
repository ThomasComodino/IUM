package com.example.superspan.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.superspan.data.Product
import com.example.superspan.databinding.ItemProductBinding

class ProductAdapter(
    private val products: List<Product>,
    private val onProductClick: (Product) -> Unit,
    private val onAddClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    // Colleghiamo l'XML item_product
    inner class ProductViewHolder(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        with(holder.binding) {
            tvName.text = product.name

            // Se c'è uno sconto mostra quello, altrimenti il prezzo base
            val finalPrice = product.discountPrice ?: product.price
            tvPrice.text = "€ " + String.format("%.2f", finalPrice)

            // Quando l'utente clicca su tutta la card (per vedere i dettagli)
            root.setOnClickListener { onProductClick(product) }

            // Quando l'utente clicca sul bottoncino "+" arancione
            btnAdd.setOnClickListener { onAddClick(product) }
        }
    }

    override fun getItemCount() = products.size
}