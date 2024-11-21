package com.mireascanner.initial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mireascanner.features.auth.presentation.sign_up.SignUpAction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val stateMachine: MainStateMachine) : ViewModel() {
    private val _state = MutableStateFlow<MainState>(MainState.Loading)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            stateMachine.state.collect { newState ->
                _state.value = newState
            }
        }
    }
}
