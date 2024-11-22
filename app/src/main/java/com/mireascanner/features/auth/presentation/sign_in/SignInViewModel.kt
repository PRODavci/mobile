package com.mireascanner.features.auth.presentation.sign_in

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mireascanner.features.auth.presentation.sign_up.SignUpEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(private val stateMachine: SignInStateMachine) : ViewModel() {
    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SignInEffect>()
    val effect = _effect.asSharedFlow()

    init {
        viewModelScope.launch {
            stateMachine.state.collect {
                _state.value = it
            }
        }

        viewModelScope.launch {
            stateMachine.effect.collect{
                _effect.emit(it)
            }
        }
    }

    fun handleAction(action: SignInAction){
        viewModelScope.launch {
            stateMachine.dispatch(action)
        }
    }

}