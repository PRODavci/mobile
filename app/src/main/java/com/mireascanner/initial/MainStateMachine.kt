package com.mireascanner.initial

import android.util.Log
import com.freeletics.flowredux.dsl.FlowReduxStateMachine
import com.mireascanner.common.auth.domain.AuthRepository
import com.mireascanner.common.utils.Result
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
                    val result =
                        authRepository.signUp("a.rodionov@edu.centralcuni.ru", "bebrikiRR2222")
                    when (result) {
                        is Result.Success -> {
                            Log.d("SignUpResult", result.data.toString())
                        }

                        is Result.Error -> {
                            Log.d("SignUpResult", result.exception.toString())
                        }
                    }
                    state.override { MainState.ContentState(true) }
                }
            }

            inState<MainState.ContentState> { }
        }
    }
}