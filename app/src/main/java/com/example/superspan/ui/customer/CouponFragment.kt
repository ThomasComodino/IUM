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

class CouponFragment : Fragment() {
    private var _binding: FragmentCouponBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentCouponBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // --- 1. GESTIONE COUPON SCONTO (PASTA) ---

        // Se Daniela (Admin) ha deciso di non pubblicare il coupon, lo nascondiamo
        if (!FakeRepository.isCouponPublishedByAdmin) {
            binding.layoutCoupon.root.visibility = View.GONE
            binding.tvNoCoupons.visibility = View.VISIBLE
        } else {
            binding.layoutCoupon.root.visibility = View.VISIBLE
            binding.tvNoCoupons.visibility = View.GONE

            // Controllo se Michele lo aveva già attivato
            if (FakeRepository.isPastaCouponActive) {
                setPastaCouponAsActivated()
            }

            binding.layoutCoupon.btnActivate.setOnClickListener {
                FakeRepository.isPastaCouponActive = true
                setPastaCouponAsActivated()
                Toast.makeText(requireContext(), "Sconto Pasta Attivato!", Toast.LENGTH_SHORT).show()
            }
        }

        // --- 2. GESTIONE COUPON REGALO (A SCELTA) ---

        val gift = FakeRepository.giftCoupon

        // Se è già stato attivato in precedenza, mostriamo il codice a barre
        if (gift.isActivated) {
            setGiftCouponAsActivated()
        } else {
            // Popoliamo i nomi dei 3 prodotti tra cui scegliere
            binding.layoutGift.rbOpt1.text = "${gift.options[0].name} (Gratis)"
            binding.layoutGift.rbOpt2.text = "${gift.options[1].name} (Gratis)"
            binding.layoutGift.rbOpt3.text = "${gift.options[2].name} (Gratis)"

            // Quando Michele sceglie un'opzione, abilitiamo il tasto Attiva
            binding.layoutGift.rgOptions.setOnCheckedChangeListener { _, checkedId ->
                binding.layoutGift.btnActivateGift.isEnabled = true
                binding.layoutGift.btnActivateGift.text = "ATTIVA REGALO"

                // Salviamo l'ID del prodotto scelto nel repository
                gift.selectedProductId = when(checkedId) {
                    R.id.rbOpt1 -> gift.options[0].id
                    R.id.rbOpt2 -> gift.options[1].id
                    else -> gift.options[2].id
                }
            }

            // Click sul tasto Attiva Regalo
            binding.layoutGift.btnActivateGift.setOnClickListener {
                gift.isActivated = true
                setGiftCouponAsActivated()
                Toast.makeText(requireContext(), "Regalo attivato! Ritiralo in negozio.", Toast.LENGTH_LONG).show()
            }
        }
    }

    // Funzione per aggiornare la grafica del Coupon Sconto
    private fun setPastaCouponAsActivated() {
        binding.layoutCoupon.btnActivate.text = "ATTIVATO"
        binding.layoutCoupon.btnActivate.isEnabled = false
        binding.layoutCoupon.chipStatus.text = "ATTIVO"
        binding.layoutCoupon.chipStatus.setChipBackgroundColorResource(android.R.color.darker_gray)
    }

    // Funzione per aggiornare la grafica del Coupon Regalo
    private fun setGiftCouponAsActivated() {
        binding.layoutGift.tvGiftTitle.text = "REGALO ATTIVO"
        binding.layoutGift.rgOptions.visibility = View.GONE
        binding.layoutGift.btnActivateGift.visibility = View.GONE
        binding.layoutGift.tvBarcode.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}