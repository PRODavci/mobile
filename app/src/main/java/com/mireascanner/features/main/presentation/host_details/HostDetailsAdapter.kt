package com.mireascanner.features.main.presentation.host_details

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mireascanner.R
import com.mireascanner.common.main.data.remote.model.HostServiceResponse

class HostServiceAdapter : ListAdapter<HostServiceResponse, HostServiceAdapter.HostServiceViewHolder>(HostServiceDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HostServiceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.host_item, parent, false)
        return HostServiceViewHolder(view)
    }

    override fun onBindViewHolder(holder: HostServiceViewHolder, position: Int) {
        val service = getItem(position)
        Log.d("HostDetailsFragment", "service ${service}")

        holder.bind(service)
    }

    class HostServiceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.textViewName)
        private val protocolTextView: TextView = itemView.findViewById(R.id.textViewProtocol)
        private val portTextView: TextView = itemView.findViewById(R.id.textViewPort)
        private val productVersionTextView: TextView = itemView.findViewById(R.id.textViewProductVersion)
        private val ostypeTextView: TextView = itemView.findViewById(R.id.textViewOstype)
        private val confTextview: TextView = itemView.findViewById(R.id.textViewConf)
//        private val idHostIdTextview : TextView = itemView.findViewById(R.id.textViewIdHostId)

        fun bind(service: HostServiceResponse) {
            nameTextView.text = service.name ?: "N/A"
            protocolTextView.text = service.protocol
            portTextView.text = service.port.toString()
            productVersionTextView.text = if (service.product != null && service.version != null) {
                "${service.product} v${service.version}"
            } else {
                "N/A"
            }
            ostypeTextView.text = service.ostype ?: "N/A"
            confTextview.text = service.conf
//            idHostIdTextview.text = "${service.id}, ${service.host_id}"
        }
    }

    class HostServiceDiffCallback : DiffUtil.ItemCallback<HostServiceResponse>() {
        override fun areItemsTheSame(oldItem: HostServiceResponse, newItem: HostServiceResponse): Boolean {
            return oldItem.id == newItem.id // Сравниваем по уникальному идентификатору
        }

        override fun areContentsTheSame(oldItem: HostServiceResponse, newItem: HostServiceResponse): Boolean {
            return oldItem == newItem // Сравниваем содержимое объектов
        }
    }
}