package com.example.superspan.ui.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.superspan.data.JobApplication
import com.example.superspan.databinding.ItemApplicationBinding

class ApplicationAdapter(private val applications: List<JobApplication>) :
    RecyclerView.Adapter<ApplicationAdapter.ApplicationViewHolder>() {

    class ApplicationViewHolder(val binding: ItemApplicationBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApplicationViewHolder {
        val binding = ItemApplicationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ApplicationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ApplicationViewHolder, position: Int) {
        val application = applications[position]
        with(holder.binding) {
            tvCandidateName.text = application.candidateName
            tvRole.text = application.role
            
            // Logica bottone Video CV: disabilitato se non presente
            btnVideoCv.isEnabled = application.hasVideoCv
            
            btnVideoCv.setOnClickListener {
                Toast.makeText(it.context, "Apertura Video CV di ${application.candidateName}", Toast.LENGTH_SHORT).show()
            }

            root.setOnClickListener {
                Toast.makeText(it.context, "Dettagli candidatura: ${application.candidateName}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun getItemCount() = applications.size
}
