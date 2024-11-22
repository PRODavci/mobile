package com.mireascanner.features.auth.presentation.sign_up

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val stateMachine: SignUpStateMachine) :
    ViewModel() {
    private val _state = MutableStateFlow<SignUpState>(SignUpState.Content)
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            stateMachine.state.collect { newState ->
                _state.value = newState
            }
        }
    }

    fun handleAction(action: SignUpAction) {
        viewModelScope.launch {
            stateMachine.dispatch(action)
        }
    }
}