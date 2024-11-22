package com.mireascanner.features.auth.presentation.sign_in

import com.mireascanner.R
import com.mireascanner.common.auth.domain.AuthRepository
import com.mireascanner.common.auth.domain.usecase.validate_email.ValidateEmailUseCase
import com.mireascanner.common.auth.domain.usecase.validate_email.ValidationEmailResult
import com.mireascanner.common.utils.BaseStateMachine
import com.mireascanner.common.utils.Result
import com.mireascanner.common.utils.UIText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class SignInStateMachine @Inject constructor(
    private val authRepository: AuthRepository,
    private val validateEmailUseCase: ValidateEmailUseCase
) :
    BaseStateMachine<SignInState, SignInAction, SignInEffect>(initialState = SignInState()) {

    private var isEmailCorrect: Boolean = false
    private var isPasswordNotEmpty = false

    init {
        spec {
            inState<SignInState> {
                on<SignInAction.EmailChanged> { action, state ->
                    if (validateEmailUseCase(action.email) is ValidationEmailResult.Failed) {
                        isEmailCorrect = false
                        return@on state.mutate {
                            state.snapshot.copy(
                                emailError = UIText.StringResource(
                                    R.string.error_incorrect_email
                                ), isSignInButtonEnabled = false
                            )
                        }
                    }
                    isEmailCorrect = true
                    return@on state.mutate {
                        state.snapshot.copy(
                            emailError = null,
                            isSignInButtonEnabled = isPasswordNotEmpty && isEmailCorrect
                        )
                    }

                }
                on<SignInAction.PasswordChanged> { action, state ->
                    isPasswordNotEmpty = action.password.isNotEmpty()
                    state.mutate { state.snapshot.copy(isSignInButtonEnabled = isEmailCorrect && isPasswordNotEmpty) }
                }
                on<SignInAction.SignIn> { action: SignInAction.SignIn, state ->
                    if (!isEmailCorrect) {
                        return@on state.noChange()
                    }
                    updateEffect(SignInEffect.ShowLoading)
                    val result = authRepository.signIn(action.email, action.password)
                    updateEffect(SignInEffect.HideLoadingDialog)
                    when (result) {
                        is Result.Success -> {
                            updateEffect(SignInEffect.NavigateToMain)
                            state.noChange()
                        }

                        is Result.Error -> {
                            state.override {
                                SignInState(
                                    error = UIText.StringResource(R.string.error_something_went_wrong),
                                    isSignInButtonEnabled = true
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}