package com.mireascanner.features.main.presentation.add_ips.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mireascanner.common.main.domain.models.AddIp
import com.mireascanner.common.utils.setOnClickListenerSafely
import com.mireascanner.databinding.InputIpItemBinding

class AddIpsAdapter(
    private val onDeleteClick: (AddIp) -> Unit,
) :
    ListAdapter<AddIp, AddIpsAdapter.ViewHolder>(AddIpDiffCallback()) {

    class ViewHolder(private val itemBinding: InputIpItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(ipItem: AddIp, onDeleteClick: (AddIp) -> Unit) {
            itemBinding.ipText.text = ipItem.ip
            itemBinding.deleteButton.setOnClickListenerSafely {
                onDeleteClick(ipItem)
                itemBinding.deleteButton.isEnabled = false
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = InputIpItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onDeleteClick)
    }

}

class AddIpDiffCallback : DiffUtil.ItemCallback<AddIp>() {
    override fun areItemsTheSame(oldItem: AddIp, newItem: AddIp): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: AddIp, newItem: AddIp): Boolean {
        return oldItem == newItem
    }
}