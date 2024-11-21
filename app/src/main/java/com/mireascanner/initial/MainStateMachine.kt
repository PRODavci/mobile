package com.mireascanner.initial

import com.freeletics.flowredux.dsl.FlowReduxStateMachine
import com.mireascanner.common.auth.domain.AuthRepository
import com.mireascanner.common.exceptions.UnauthorizedException
import com.mireascanner.common.utils.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class MainStateMachine @Inject constructor(private val authRepository: AuthRepository) :
    FlowReduxStateMachine<MainState, MainAction>(initialState = MainState.Loading) {

    init {
        spec {
            inState<MainState.Loading> {
                onEnter { state ->
                    val result =
                        authRepository.checkUserDataSafely()
                    when (result) {
                        is Result.Success -> {
                            state.override { MainState.ContentState(true) }
                        }

                        is Result.Error -> {
                            if (result.exception is UnauthorizedException) {
                                state.override { MainState.ContentState(false) }
                            } else {
                                state.override { MainState.ContentState( true) }
                            }
                        }
                    }
                }
            }

            inState<MainState.ContentState> { }
        }
    }
}