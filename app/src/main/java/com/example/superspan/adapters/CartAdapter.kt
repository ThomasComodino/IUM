package com.example.superspan.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.superspan.data.CartItem
import com.example.superspan.data.FakeRepository
import com.example.superspan.databinding.ItemCartBinding

class CartAdapter(
    private var cartItems: List<CartItem>,
    private val onQuantityChange: () -> Unit,
    private val onItemClick: (Int) -> Unit,
    private val isReadOnly: Boolean = false
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

            val currentPrice = FakeRepository.getFinalPrice(item.product)
            tvCartPrice.text = "€ " + String.format("%.2f", currentPrice)

            tvQuantity.text = item.quantity.toString()
            tvReadOnlyQuantity.text = "Qtà: ${item.quantity}"

            if (isReadOnly) {
                layoutQuantityControls.visibility = View.GONE
                tvReadOnlyQuantity.visibility = View.VISIBLE
                btnDelete.visibility = View.GONE
                root.setOnClickListener(null)
            } else {
                layoutQuantityControls.visibility = View.VISIBLE
                tvReadOnlyQuantity.visibility = View.GONE
                btnDelete.visibility = View.VISIBLE

                btnPlus.setOnClickListener {
                    item.quantity++
                    notifyItemChanged(position)
                    onQuantityChange()
                }

                btnMinus.setOnClickListener {
                    if (item.quantity > 1) {
                        item.quantity--
                        notifyItemChanged(position)
                        onQuantityChange()
                    }
                }

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

                root.setOnClickListener {
                    onItemClick(item.product.id)
                }
            }
        }
    }

    override fun getItemCount() = cartItems.size
}
