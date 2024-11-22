package com.mireascanner.common.utils

import com.freeletics.flowredux.dsl.FlowReduxStateMachine
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

@OptIn(ExperimentalCoroutinesApi::class)
open class BaseStateMachine<S : Any, A : Any, E : Any>(initialState: S) :
    FlowReduxStateMachine<S, A>(initialState) {

    private val _effect = MutableSharedFlow<E>()
    val effect = _effect.asSharedFlow()

    protected suspend fun updateEffect(effect: E) {
        _effect.emit(effect)
    }
}