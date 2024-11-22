package com.mireascanner.features.auth.presentation.sign_in

import android.util.Log
import com.freeletics.flowredux.dsl.ChangedState
import com.freeletics.flowredux.dsl.FlowReduxStateMachine
import com.freeletics.flowredux.dsl.State
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
class SignInStateMachine @Inject constructor(
    private val authRepository: AuthRepository,
    private val validateEmailUseCase: ValidateEmailUseCase
) :
    BaseStateMachine<SignInState, SignInAction, SignInEffect>(initialState = SignInState()) {

    private var isEmailCorrect: Boolean = false

    init {
        spec {
            inState<SignInState> {
                on<SignInAction.EmailChanged> { action, state ->
                    if (validateEmailUseCase(action.email) is ValidationEmailResult.Failed) {
                        isEmailCorrect = false
                        return@on state.override { SignInState(emailError = UIText.StringResource(R.string.error_incorrect_email), isSignInButtonEnabled = false) }
                    }
                    isEmailCorrect = true
                    return@on state.override { SignInState(isSignInButtonEnabled = true) }

                }
                on<SignInAction.SignIn> { action: SignInAction.SignIn, state ->
                    if(!isEmailCorrect){
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
                            Log.e("SignInStateMachine", result.exception.message, result.exception)
                            state.override { SignInState(error = UIText.StringResource(R.string.error_something_went_wrong), isSignInButtonEnabled = true) }
                        }
                    }
                }
            }
        }
    }
}