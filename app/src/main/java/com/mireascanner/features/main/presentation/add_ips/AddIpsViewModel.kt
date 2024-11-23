package com.mireascanner.features.main.presentation.add_ips

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mireascanner.common.main.domain.models.AddIp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddIpsViewModel @Inject constructor() : ViewModel() {

    private val ips = mutableMapOf<Int, String>()
    private val _ipsState = MutableStateFlow(emptyList<AddIp>())
    val ipsState = _ipsState.asStateFlow()

    private var increment = 0


    fun addIp(ip: String) {
        viewModelScope.launch {
            val id = ++increment
            val addIp = AddIp(id, ip)
            ips[id] = ip
            val list = ipsState.value.toMutableList()
            list.add(addIp)
            _ipsState.value = list.toList()
        }
    }

    fun removeAddIp(addIp: AddIp) {
        viewModelScope.launch {
            ips.remove(addIp.id)
            val list = ipsState.value.toMutableList()
            list.removeAt(list.indexOfLast { it.id == addIp.id })
            _ipsState.value = list.toList()
        }
    }
}