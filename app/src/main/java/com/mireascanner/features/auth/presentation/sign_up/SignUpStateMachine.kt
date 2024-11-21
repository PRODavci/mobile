package com.mireascanner.features.auth.presentation.sign_up

import com.freeletics.flowredux.dsl.FlowReduxStateMachine
import com.mireascanner.common.auth.data.remote.models.SignBody
import com.mireascanner.common.auth.data.remote.repository.RemoteAuthRepository
import com.mireascanner.common.auth.data.utils.toDomain
import com.mireascanner.common.utils.Result
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class SignUpStateMachine @Inject constructor(authRepository: RemoteAuthRepository) :
    FlowReduxStateMachine<SignUpState, SignUpAction>(initialState = SignUpState.Content) {
    init {
        spec {
            inState<SignUpState.Content> {
                on<SignUpAction.SignUp> { action: SignUpAction.SignUp, state ->
                    state.override { SignUpState.Loading }
                    val result = authRepository.signUp(
                        SignBody(
                            action.email,
                            action.password
                        )
                    )
                    when (result) {
                        is Result.Success -> {
                            state.override { SignUpState.Succeed(result.data.user.toDomain()) }
                        }

                        is Result.Error -> {
                            state.override { SignUpState.Failed(result.exception) }
                        }
                    }
                }
            }
        }
    }
}