package com.example.superspan.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.superspan.data.CartItem
import com.example.superspan.databinding.ItemCartBinding

class CartAdapter(
    private var cartItems: List<CartItem>,
    private val onQuantityChange: () -> Unit // Questo avviserà la pagina di ricalcolare il totale
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(val binding: ItemCartBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = ItemCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val item = cartItems[position]
        with(holder.binding) {
            tvCartName.text = item.product.name

            val price = item.product.discountPrice ?: item.product.price
            tvCartPrice.text = "€ " + String.format("%.2f", price)
            tvQuantity.text = item.quantity.toString()

            // Click su Più
            btnPlus.setOnClickListener {
                item.quantity++
                notifyItemChanged(position)
                onQuantityChange() // Aggiorna il totale
            }

            // Click su Meno
            btnMinus.setOnClickListener {
                if (item.quantity > 1) {
                    item.quantity--
                    notifyItemChanged(position)
                    onQuantityChange()
                }
            }

            // Click su Cestino
            btnDelete.setOnClickListener {
                com.google.android.material.dialog.MaterialAlertDialogBuilder(holder.itemView.context)
                    .setTitle("Rimuovere prodotto?")
                    .setMessage("Sei sicuro di voler togliere questo articolo dal carrello?")
                    .setNegativeButton("Annulla", null)
                    .setPositiveButton("Rimuovi") { _, _ ->
                        (cartItems as MutableList).removeAt(position)
                        notifyDataSetChanged()
                        onQuantityChange()
                    }
                    .show()
            }
        }
    }

    override fun getItemCount() = cartItems.size
}