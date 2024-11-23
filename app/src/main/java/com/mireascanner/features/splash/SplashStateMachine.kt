package com.mireascanner.features.splash

import android.util.Log
import com.mireascanner.common.auth.domain.AuthRepository
import com.mireascanner.common.exceptions.UnauthorizedException
import com.mireascanner.common.utils.BaseStateMachine
import com.mireascanner.common.utils.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class SplashStateMachine @Inject constructor(private val authRepository: AuthRepository) :
    BaseStateMachine<SplashState, SplashAction, Unit>(initialState = SplashState.Loading) {

    init {
        spec {
            inState<SplashState.Loading> {
                onEnter { state ->
                    val result =
                        authRepository.checkUserDataSafely()
                    Log.d("SplashStateMachine", result.toString())
                    when (result) {
                        is Result.Success -> {
                            state.override { SplashState.ContentState(true) }
                        }

                        is Result.Error -> {
                            if (result.exception is UnauthorizedException) {
                                state.override { SplashState.ContentState(false) }
                            } else {
                                state.override { SplashState.ContentState(true) }
                            }
                        }
                    }
                }
            }

            inState<SplashState.ContentState> { }
        }
    }
}