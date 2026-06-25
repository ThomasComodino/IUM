package com.example.superspan.ui.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.superspan.R
import com.example.superspan.data.FakeRepository
import com.example.superspan.databinding.FragmentCouponBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class CouponFragment : Fragment() {
    private var _binding: FragmentCouponBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCouponBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Se tutti i coupon sono disattivati, mostriamo il messaggio di vuoto
        if (!FakeRepository.isPastaCouponPublished && !FakeRepository.isGiftCouponPublished && !FakeRepository.isShopOnlyCouponPublished) {
            binding.layoutCoupon.root.visibility = View.GONE
            binding.layoutShopOnly.root.visibility = View.GONE
            binding.layoutGift.root.visibility = View.GONE
            binding.tvNoCoupons.visibility = View.VISIBLE
            return
        }

        binding.tvNoCoupons.visibility = View.GONE

        // --- 1. GESTIONE COUPON SCONTO (ALIMENTARI) ---
        if (FakeRepository.isPastaCouponPublished) {
            binding.layoutCoupon.root.visibility = View.VISIBLE
            binding.layoutCoupon.tvCouponTitle.text = "Sconto 15% su tutti gli Alimentari"
            
            if (FakeRepository.isPastaCouponActive) {
                setCouponAsActivated(binding.layoutCoupon, "SP-215-OFFER")
            }

            binding.layoutCoupon.btnActivate.setOnClickListener {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Attiva Coupon")
                    .setMessage("Vuoi attivare lo sconto del 15% sugli Alimentari?")
                    .setNegativeButton("Annulla", null)
                    .setPositiveButton("Attiva") { _, _ ->
                        FakeRepository.isPastaCouponActive = true
                        setCouponAsActivated(binding.layoutCoupon, "SP-215-OFFER")
                        Toast.makeText(requireContext(), "Sconto Alimentari Attivato!", Toast.LENGTH_SHORT).show()
                    }.show()
            }
        } else {
            binding.layoutCoupon.root.visibility = View.GONE
        }

        // --- 2. GESTIONE COUPON SOLO NEGOZIO ---
        if (FakeRepository.isShopOnlyCouponPublished) {
            binding.layoutShopOnly.root.visibility = View.VISIBLE
            binding.layoutShopOnly.tvCouponTitle.text = "Sconto 20% Casalinghi (Solo in Negozio)"
            binding.layoutShopOnly.ivCoupon.setImageResource(R.drawable.ic_coupon)
            
            if (FakeRepository.isShopOnlyCouponActive) {
                setCouponAsActivated(binding.layoutShopOnly, "SP-442-OFFER")
            }

            binding.layoutShopOnly.btnActivate.setOnClickListener {
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Attiva Coupon")
                    .setMessage("Vuoi attivare lo sconto del 20% sui Casalinghi da usare in negozio?")
                    .setNegativeButton("Annulla", null)
                    .setPositiveButton("Attiva") { _, _ ->
                        FakeRepository.isShopOnlyCouponActive = true
                        setCouponAsActivated(binding.layoutShopOnly, "SP-442-OFFER")
                        Toast.makeText(requireContext(), "Coupon Negozio Attivato!", Toast.LENGTH_SHORT).show()
                    }.show()
            }
        } else {
            binding.layoutShopOnly.root.visibility = View.GONE
        }

        // --- 3. GESTIONE COUPON REGALO (A SCELTA) ---
        if (FakeRepository.isGiftCouponPublished) {
            binding.layoutGift.root.visibility = View.VISIBLE
            val gift = FakeRepository.giftCoupon

            if (gift.isActivated) {
                setGiftCouponAsActivated()
            } else {
                binding.layoutGift.rbOpt1.text = "${gift.options[0].name} (Gratis)"
                binding.layoutGift.rbOpt2.text = "${gift.options[1].name} (Gratis)"
                binding.layoutGift.rbOpt3.text = "${gift.options[2].name} (Gratis)"

                binding.layoutGift.rgOptions.setOnCheckedChangeListener { _, checkedId ->
                    binding.layoutGift.btnActivateGift.isEnabled = true
                    binding.layoutGift.btnActivateGift.text = "ATTIVA REGALO"

                    gift.selectedProductId = when(checkedId) {
                        R.id.rbOpt1 -> gift.options[0].id
                        R.id.rbOpt2 -> gift.options[1].id
                        else -> gift.options[2].id
                    }
                }

                binding.layoutGift.btnActivateGift.setOnClickListener {
                    MaterialAlertDialogBuilder(requireContext())
                        .setTitle("Attiva Regalo")
                        .setMessage("Confermi la scelta del prodotto omaggio?")
                        .setNegativeButton("Annulla", null)
                        .setPositiveButton("Conferma") { _, _ ->
                            gift.isActivated = true
                            setGiftCouponAsActivated()
                            Toast.makeText(requireContext(), "Regalo attivato! Ritiralo in negozio.", Toast.LENGTH_LONG).show()
                        }.show()
                }
            }
        } else {
            binding.layoutGift.root.visibility = View.GONE
        }
    }

    private fun setCouponAsActivated(layoutBinding: com.example.superspan.databinding.ItemCouponBinding, code: String) {
        // Nascondiamo il tasto attiva e mostriamo il codice direttamente
        layoutBinding.btnActivate.visibility = View.GONE
        layoutBinding.tvBarcode.visibility = View.VISIBLE
        layoutBinding.tvBarcode.text = "CODICE: $code"

        layoutBinding.chipStatus.text = "ATTIVO"
        layoutBinding.chipStatus.setChipBackgroundColorResource(android.R.color.darker_gray)
    }

    private fun setGiftCouponAsActivated() {
        val gift = FakeRepository.giftCoupon
        val selectedProduct = gift.options.find { it.id == gift.selectedProductId }
        
        binding.layoutGift.tvGiftTitle.text = "REGALO ATTIVO"
        
        // Se abbiamo un prodotto selezionato, lo indichiamo nel titolo
        selectedProduct?.let {
            binding.layoutGift.tvGiftTitle.text = "REGALO ATTIVO: ${it.name}"
        }

        binding.layoutGift.rgOptions.visibility = View.GONE
        binding.layoutGift.btnActivateGift.visibility = View.GONE
        
        binding.layoutGift.tvBarcode.visibility = View.VISIBLE
        binding.layoutGift.tvBarcode.text = "CODICE: SP-998-GIFT"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
