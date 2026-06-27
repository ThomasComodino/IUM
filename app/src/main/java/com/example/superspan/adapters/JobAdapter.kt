package com.example.superspan.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.superspan.data.JobApplication
import com.example.superspan.databinding.ItemJobApplicationBinding

class JobAdapter(
    private val list: List<JobApplication>,
    private val onItemClick: (JobApplication) -> Unit // Funzione per gestire il click sulla card
) : RecyclerView.Adapter<JobAdapter.JobViewHolder>() {

    // Il ViewHolder collega il codice agli elementi grafici dell'XML item_job_application
    inner class JobViewHolder(val binding: ItemJobApplicationBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        // "Gonfiamo" il layout della singola riga
        val binding = ItemJobApplicationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return JobViewHolder(binding)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val application = list[position]

        with(holder.binding) {
            // Inseriamo i dati del candidato nelle TextView
            tvCandidateName.text = application.candidateName
            tvRole.text = "Ruolo: ${application.role}"
            tvDate.text = "Inviato il: ${application.date}"

            // Gestione del click sul pulsante "APRI CANDIDATURA"
            btnViewDetails.setOnClickListener {
                onItemClick(application)
            }

            // Click sulla card rimosso come richiesto (si usa solo il pulsante)
            root.setOnClickListener(null)
            root.isClickable = false
        }
    }

    override fun getItemCount(): Int = list.size
}