package com.example.superspan.ui.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.superspan.R

class StoreSelectionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_store_selection, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rgStores = view.findViewById<android.widget.RadioGroup>(R.id.rgStores)

        view.findViewById<Button>(R.id.btnNext).setOnClickListener {
            val selectedId = rgStores.checkedRadioButtonId
            val selectedStore = view.findViewById<android.widget.RadioButton>(selectedId).text.toString()
            
            val bundle = Bundle().apply {
                putString("selectedStore", selectedStore)
            }
            findNavController().navigate(R.id.action_storeSelectionFragment_to_checkoutFragment, bundle)
        }
    }
}