package com.mireascanner.initial

import com.freeletics.flowredux.dsl.FlowReduxStateMachine
import com.mireascanner.common.auth.domain.AuthRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class MainStateMachine @Inject constructor(private val authRepository: AuthRepository) :
    FlowReduxStateMachine<MainState, MainAction>(initialState = MainState.Loading) {

    init {
        spec {
            inState<MainState.Loading> {
                onEnter { state ->
                    delay(5000)
                    state.override { MainState.ContentState(true) }
                }
            }

            inState<MainState.ContentState> { }
        }
    }
}