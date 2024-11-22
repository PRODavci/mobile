package com.mireascanner.features.auth.presentation.sign_up

import android.util.Log
import com.mireascanner.R
import com.mireascanner.common.auth.domain.AuthRepository
import com.mireascanner.common.auth.domain.usecase.validate_email.ValidateEmailUseCase
import com.mireascanner.common.auth.domain.usecase.validate_email.ValidationEmailResult
import com.mireascanner.common.auth.domain.usecase.validate_password.ValidatePasswordUseCase
import com.mireascanner.common.auth.domain.usecase.validate_password.ValidationPasswordResult
import com.mireascanner.common.utils.BaseStateMachine
import com.mireascanner.common.utils.Result
import com.mireascanner.common.utils.UIText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class SignUpStateMachine @Inject constructor(
    private val authRepository: AuthRepository,
    private val validateEmailUseCase: ValidateEmailUseCase,
    private val validatePasswordUseCase: ValidatePasswordUseCase
) :
    BaseStateMachine<SignUpState, SignUpAction, SignUpEffect>(initialState = SignUpState()) {

    private var isEmailCorrect: Boolean = false
    private var isPasswordCorrect: Boolean = false

    init {
        spec {
            inState<SignUpState> {
                on<SignUpAction.PasswordChanged> { action, state ->
                    if (validatePasswordUseCase(action.password) !is ValidationPasswordResult.Success) {
                        val errorResId = when (validatePasswordUseCase(action.password)) {
                            is ValidationPasswordResult.VeryShortPassword -> {
                                R.string.error_password_too_short
                            }

                            is ValidationPasswordResult.NotContainsLettersOrDigits -> {
                                R.string.error_not_contanis_letters_or_digits
                            }

                            is ValidationPasswordResult.NotContainsLowerOrUpperCase -> {
                                R.string.error_not_contains_upper_or_lower_letters
                            }

                            else -> {
                                R.string.error_something_went_wrong
                            }
                        }
                        isPasswordCorrect = false
                        return@on state.override {
                            SignUpState(
                                passwordError = UIText.StringResource(
                                    errorResId
                                ),
                                isSignUpButtonAvailable = false
                            )
                        }
                    }
                    isPasswordCorrect = true
                    return@on state.override { SignUpState(isSignUpButtonAvailable = isCredentialsCorrect()) }
                }

                on<SignUpAction.EmailChanged> { action, state ->
                    if (validateEmailUseCase(action.email) is ValidationEmailResult.Failed) {
                        isEmailCorrect = false
                        return@on state.override {
                            SignUpState(
                                emailError = UIText.StringResource(
                                    R.string.error_incorrect_email
                                ),
                                isSignUpButtonAvailable = false
                            )
                        }
                    }
                    isEmailCorrect = true
                    state.mutate { state.snapshot.copy(isSignUpButtonAvailable = isCredentialsCorrect()) }
                }
                on<SignUpAction.SignUp> { action: SignUpAction.SignUp, state ->
                    Log.d("SignUpStateMachine", validatePasswordUseCase(action.password).toString())
                    updateEffect(SignUpEffect.ShowLoading)
                    if(!isCredentialsCorrect()){
                        return@on state.noChange()
                    }
                    val result = authRepository.signUp(
                        action.email,
                        action.password
                    )
                    updateEffect(SignUpEffect.HideLoadingDialog)
                    when (result) {
                        is Result.Success -> {
                            updateEffect(SignUpEffect.NavigateToMain)
                            state.noChange()
                        }

                        is Result.Error -> {
                            Log.e("SignUpStateMachine", result.exception.message, result.exception)
                            state.override {
                                SignUpState(
                                    error = UIText.StringResource(R.string.error_something_went_wrong),
                                    isSignUpButtonAvailable = true
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun isCredentialsCorrect() = isEmailCorrect && isPasswordCorrect


}