package com.mireascanner.features.main.presentation.scan.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mireascanner.R
import com.mireascanner.common.main.domain.models.Host

class HostsAdapter(
    private val hosts: Array<Host>,
    private val onClick: (host: Int) -> Unit
) : RecyclerView.Adapter<HostsAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val hostView: TextView = view.findViewById(R.id.tv_recycler_text)
        fun bind(host: Host){
            hostView.text = host.ip

            itemView.setOnClickListener {
                onClick(host.id)
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