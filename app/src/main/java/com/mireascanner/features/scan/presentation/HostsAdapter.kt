package com.mireascanner.features.scan.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mireascanner.R

class HostsAdapter(
    private val hosts: Array<String>,
    private val onClick: (host: String) -> Unit
) : RecyclerView.Adapter<HostsAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val hostView: TextView = view.findViewById(R.id.tv_recycler_text)
        fun bind(host: String){
            hostView.text = host

            itemView.setOnClickListener {
                onClick(host)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_text, parent, false)
        )
    }

    override fun getItemCount(): Int = hosts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val host = hosts[position]

        holder.bind(host)
    }
}