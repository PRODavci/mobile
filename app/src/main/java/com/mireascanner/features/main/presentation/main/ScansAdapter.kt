package com.mireascanner.features.main.presentation.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mireascanner.R
import com.mireascanner.common.main.domain.models.Scan

class ScansAdapter(
    private val context: Context,
    private val scans: Array<Scan>,
    private val onClick: (scan: Scan) -> Unit
) : RecyclerView.Adapter<ScansAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val timeStamp: TextView = view.findViewById(R.id.tv_scan_timestamp)
        fun bind(scan: Scan) {
            timeStamp.text = context.getString(R.string.text_scanning) + ": " + scan.timeStamp
            itemView.setOnClickListener {
                onClick(scan)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_scan, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return scans.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val scan = scans[position]

        holder.bind(scan)
    }
}