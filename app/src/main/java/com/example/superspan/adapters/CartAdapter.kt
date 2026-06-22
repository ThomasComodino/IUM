package com.example.superspan.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.superspan.data.CartItem
import com.example.superspan.data.FakeRepository
import com.example.superspan.databinding.ItemCartBinding

class CartAdapter(
    private var cartItems: List<CartItem>,
    private val onQuantityChange: () -> Unit,
    private val onItemClick: (Int) -> Unit
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

            // --- CAMBIAMENTO QUI: Usa getFinalPrice invece di discountPrice ---
            val currentPrice = FakeRepository.getFinalPrice(item.product)
            tvCartPrice.text = "€ " + String.format("%.2f", currentPrice)

            tvQuantity.text = item.quantity.toString()

            // Click su Più
            btnPlus.setOnClickListener {
                item.quantity++
                notifyItemChanged(position)
                onQuantityChange()
            }

            // Click su Meno
            btnMinus.setOnClickListener {
                if (item.quantity > 1) {
                    item.quantity--
                    notifyItemChanged(position)
                    onQuantityChange()
                }
            }

            // Click su Cestino (con dialogo di conferma come chiesto)
            btnDelete.setOnClickListener {
                com.google.android.material.dialog.MaterialAlertDialogBuilder(holder.itemView.context)
                    .setTitle("Rimuovere prodotto?")
                    .setMessage("Sei sicuro di voler togliere questo articolo?")
                    .setNegativeButton("Annulla", null)
                    .setPositiveButton("Rimuovi") { _, _ ->
                        (cartItems as MutableList).removeAt(position)
                        notifyDataSetChanged()
                        onQuantityChange()
                    }
                    .show()
            }

            // Navigazione al dettaglio al click sulla riga
            root.setOnClickListener {
                onItemClick(item.product.id)
            }
        }
    }

    override fun getItemCount() = cartItems.size
}