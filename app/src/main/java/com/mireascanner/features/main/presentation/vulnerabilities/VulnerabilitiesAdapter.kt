package com.mireascanner.features.main.presentation.vulnerabilities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mireascanner.R
import com.mireascanner.common.main.data.remote.model.VulnerabilityResponse

class VulnerabilitiesAdapter(
    private val vulnerabilities: List<VulnerabilityResponse>
) : RecyclerView.Adapter<VulnerabilitiesAdapter.ViewHolder>() {
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val product: TextView = view.findViewById(R.id.tvProductText)
        val description: TextView = view.findViewById(R.id.tvProductText)
        val baseScore: TextView = view.findViewById(R.id.tvProductText)
        val refs: TextView = view.findViewById(R.id.tvProductText)

        fun bind(vulnerability: VulnerabilityResponse){
            product.text = vulnerability.product
            description.text = vulnerability.description
            baseScore.text = vulnerability.base_score.toString()
            refs.text = vulnerability.references.joinToString(separator = ", ")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_vulneravilities, parent, false)
        )
    }

    override fun getItemCount(): Int =vulnerabilities.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val vulnerability = vulnerabilities[position]

        holder.bind(vulnerability)
    }
}