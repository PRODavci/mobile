package com.mireascanner.features.main.presentation.host_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HostDetailsViewModel @Inject constructor(
    private val stateMachine: HostDetailsStateMachine
) : ViewModel() {
    private val _state = MutableStateFlow(HostDetailsState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<HostDetailsEffect>()
    val effect = _effect.asSharedFlow()

    init {
        viewModelScope.launch {
            stateMachine.state.collect {
                _state.value = it
            }
        }
        viewModelScope.launch {
            stateMachine.effect.collect {
                _effect.emit(it)
            }
        }
    }

    fun handleAction(action: HostDetailsAction) {
        viewModelScope.launch {
            stateMachine.dispatch(action)
        }
    }
}