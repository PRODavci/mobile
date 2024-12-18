package com.mireascanner.features.main.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val stateMachine: MainStateMachine) : ViewModel() {
    private val _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<MainEffect>()
    val effect = _effect.asSharedFlow()

    init {
        viewModelScope.launch {
            stateMachine.state.collect {
                _state.value = it
            }
        }
        viewModelScope.launch {
            stateMachine.effect.collect { newEffect ->
                _effect.emit(newEffect)
            }
        }
    }

    fun handleAction(action: MainAction) {
        viewModelScope.launch {
            stateMachine.dispatch(action)
        }
    }
}