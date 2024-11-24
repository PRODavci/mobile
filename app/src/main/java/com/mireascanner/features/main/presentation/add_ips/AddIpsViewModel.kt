package com.mireascanner.features.main.presentation.add_ips

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mireascanner.common.exceptions.UnauthorizedException
import com.mireascanner.common.main.data.repository.MainRepositoryImpl
import com.mireascanner.common.main.domain.MainRepository
import com.mireascanner.common.main.domain.models.AddIp
import com.mireascanner.common.utils.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddIpsViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val ips = mutableMapOf<Int, String>()
    private val _ipsState = MutableStateFlow(emptyList<AddIp>())
    val ipsState = _ipsState.asStateFlow()

    private val _effect = MutableSharedFlow<AddIpsEffect>()
    val effect = _effect.asSharedFlow()

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

    fun saveIps() {
        viewModelScope.launch(Dispatchers.IO) {
            _effect.emit(AddIpsEffect.ShowLoading)
            when (val result = mainRepository.startScan(ipsState.value.map { it.ip })) {
                is Result.Success -> {
                    _effect.emit(AddIpsEffect.HideLoading)
                    _effect.emit(AddIpsEffect.NavigateBack)
                }

                is Result.Error -> {
                    Log.d("Tagdelete", result.exception.toString())
                    if (result.exception is UnauthorizedException) {
                        _effect.emit(AddIpsEffect.NavigateToAuth)
                    } else {
                        _effect.emit(AddIpsEffect.ShowError)
                    }
                }
            }
            _effect.emit(AddIpsEffect.HideLoading)
        }

    }
}