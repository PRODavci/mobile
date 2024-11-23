package com.mireascanner.features.main.presentation.main

import com.mireascanner.R
import com.mireascanner.common.main.data.utils.toDomain
import com.mireascanner.common.main.domain.MainRepository
import com.mireascanner.common.utils.BaseStateMachine
import com.mireascanner.common.utils.Result
import com.mireascanner.common.utils.UIText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class MainStateMachine @Inject constructor(
    private val mainRepository: MainRepository
) :
    BaseStateMachine<MainState, MainAction, MainEffect>(initialState = MainState()) {

    init {
        spec {
            inState<MainState> {
                on<MainAction.GetAllScans> { action: MainAction.GetAllScans, state ->
                    updateEffect(MainEffect.ShowLoader)
                    val result = mainRepository.getAllScans()
                    updateEffect(MainEffect.CancelLoader)
                    when (result) {
                        is Result.Success -> {
                            return@on state.override { MainState(scans = result.data.toDomain()) }
                        }

                        is Result.Error -> {
                            return@on state.override {
                                MainState(
                                    error = UIText.StringResource(
                                        R.string.error_something_went_wrong
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

}