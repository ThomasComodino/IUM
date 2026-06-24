package com.example.superspan.ui.customer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.superspan.R
import com.example.superspan.data.Address
import com.example.superspan.data.FakeRepository

class AddressesFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_addresses, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val rvAddresses = view.findViewById<RecyclerView>(R.id.rvAddresses)
        val tvNoAddresses = view.findViewById<TextView>(R.id.tvNoAddresses)

        if (FakeRepository.addresses.isEmpty()) {
            rvAddresses.visibility = View.GONE
            tvNoAddresses.visibility = View.VISIBLE
        } else {
            rvAddresses.visibility = View.VISIBLE
            tvNoAddresses.visibility = View.GONE
            rvAddresses.layoutManager = LinearLayoutManager(requireContext())
            rvAddresses.adapter = AddressesAdapter(FakeRepository.addresses)
        }

        val fabAddAddress = view.findViewById<View>(R.id.fabAddAddress)
        fabAddAddress.setOnClickListener {
            Toast.makeText(requireContext(), "Funzionalità aggiunta indirizzo in arrivo!", Toast.LENGTH_SHORT).show()
        }
    }

    class AddressesAdapter(private val addresses: List<Address>) : RecyclerView.Adapter<AddressesAdapter.AddressViewHolder>() {
        
        class AddressViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val tvName: TextView = view.findViewById(R.id.tvAddressName)
            val tvFull: TextView = view.findViewById(R.id.tvFullAddress)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_address, parent, false)
            return AddressViewHolder(view)
        }

        override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
            val address = addresses[position]
            holder.tvName.text = address.name
            holder.tvFull.text = address.fullAddress
        }

        override fun getItemCount() = addresses.size
    }
}