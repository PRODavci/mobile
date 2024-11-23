package com.mireascanner.features.main.presentation.add_ips

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mireascanner.common.main.domain.models.AddIp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random


@HiltViewModel
class AddIpsViewModel @Inject constructor() : ViewModel() {

    private val ips = mutableMapOf<Int, String>()
    private val _ipsState = MutableStateFlow(emptyList<AddIp>())
    val ipsState = _ipsState.asStateFlow()


    fun addIp(ip: String) {
        viewModelScope.launch {
            Log.d("IP", "here")
            val id = Random.nextInt(0, 999999999)
            val addIp = AddIp(id, ip)
            ips[id] = ip
            val list = ipsState.value.toMutableList()
            list.add(addIp)
            _ipsState.value = list.toList()
            Log.d("IP", ipsState.value.toString())
        }
    }

    fun removeAddIp(addIp: AddIp) {
        Log.d("IP", addIp.id.toString())
        viewModelScope.launch {
            ips.remove(addIp.id)
            val list = ipsState.value.toMutableList()
            list.removeAt(list.indexOfLast { it.id == addIp.id })
            _ipsState.value = list.toList()
        }
    }
}