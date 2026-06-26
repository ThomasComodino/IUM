package com.example.superspan.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.superspan.R
import com.example.superspan.data.Coupon
import com.example.superspan.data.FakeRepository
import com.example.superspan.databinding.ItemCouponBinding
import com.example.superspan.databinding.ItemCouponGiftBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class ClientCouponsAdapter(
    private var list: List<Coupon>,
    private val onActivate: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_STANDARD = 0
        private const val TYPE_GIFT = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position].type == "GIFT") TYPE_GIFT else TYPE_STANDARD
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_GIFT) {
            GiftViewHolder(ItemCouponGiftBinding.inflate(inflater, parent, false))
        } else {
            StandardViewHolder(ItemCouponBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val coupon = list[position]
        if (holder is StandardViewHolder) {
            holder.bind(coupon)
        } else if (holder is GiftViewHolder) {
            holder.bind(coupon)
        }
    }

    override fun getItemCount() = list.size

    inner class StandardViewHolder(val binding: ItemCouponBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(coupon: Coupon) {
            with(binding) {
                tvCouponTitle.text = coupon.title
                
                // Indichiamo se è solo per negozio
                if (!coupon.isOnline) {
                    tvCouponUsage.visibility = View.VISIBLE
                } else {
                    tvCouponUsage.visibility = View.GONE
                }

                if (coupon.isActive) {
                    val isLocallyActive = FakeRepository.activatedCouponIds.contains(coupon.id)
                    
                    if (isLocallyActive) {
                        setActivated(coupon)
                    } else {
                        btnActivate.visibility = View.VISIBLE
                        tvBarcode.visibility = View.GONE
                        btnActivate.setOnClickListener {
                            MaterialAlertDialogBuilder(root.context)
                                .setTitle("Attiva Coupon")
                                .setMessage("Vuoi attivare questo coupon?")
                                .setNegativeButton("Annulla", null)
                                .setPositiveButton("Attiva") { _, _ ->
                                    FakeRepository.activatedCouponIds.add(coupon.id)
                                    setActivated(coupon)
                                    onActivate()
                                }.show()
                        }
                    }
                }
            }
        }
        private fun setActivated(coupon: Coupon) {
            binding.btnActivate.visibility = View.GONE
            binding.chipStatus.text = "ATTIVO"
            
            if (!coupon.isOnline) {
                // Se è un coupon da negozio, mostriamo il codice
                binding.tvBarcode.visibility = View.VISIBLE
                binding.tvBarcode.text = "CODICE: SP-${100 + coupon.id}-OFFER"
            } else {
                // Se è online, non mostriamo codici, basta lo stato ATTIVO
                binding.tvBarcode.visibility = View.GONE
            }
        }
    }

    inner class GiftViewHolder(val binding: ItemCouponGiftBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(coupon: Coupon) {
            with(binding) {
                tvGiftTitle.text = coupon.title
                
                val gift = FakeRepository.giftCoupon
                if (gift.isActivated) {
                    setActivated(gift.options.find { it.id == gift.selectedProductId }?.name ?: "")
                } else {
                    rgOptions.visibility = View.VISIBLE
                    btnActivateGift.visibility = View.VISIBLE
                    tvBarcode.visibility = View.GONE

                    rbOpt1.text = gift.options[0].name
                    rbOpt2.text = gift.options[1].name
                    rbOpt3.text = gift.options[2].name

                    rgOptions.setOnCheckedChangeListener { _, checkedId ->
                        btnActivateGift.isEnabled = true
                        gift.selectedProductId = when(checkedId) {
                            R.id.rbOpt1 -> gift.options[0].id
                            R.id.rbOpt2 -> gift.options[1].id
                            else -> gift.options[2].id
                        }
                    }

                    btnActivateGift.setOnClickListener {
                        MaterialAlertDialogBuilder(root.context)
                            .setTitle("Conferma Regalo")
                            .setMessage("Confermi la scelta?")
                            .setNegativeButton("Annulla", null)
                            .setPositiveButton("Conferma") { _, _ ->
                                gift.isActivated = true
                                setActivated(gift.options.find { it.id == gift.selectedProductId }?.name ?: "")
                                onActivate()
                            }.show()
                    }
                }
            }
        }
        private fun setActivated(productName: String) {
            binding.tvGiftTitle.text = "REGALO ATTIVO: $productName"
            binding.rgOptions.visibility = View.GONE
            binding.btnActivateGift.visibility = View.GONE
            binding.tvBarcode.visibility = View.VISIBLE
            binding.tvBarcode.text = "CODICE: SP-998-GIFT"
        }
    }
}
