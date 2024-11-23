package com.mireascanner.features.main.presentation.host_details

import com.mireascanner.R
import com.mireascanner.common.main.domain.MainRepository
import com.mireascanner.common.utils.BaseStateMachine
import com.mireascanner.common.utils.Result
import com.mireascanner.common.utils.UIText
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class HostDetailsStateMachine @Inject constructor(
    private val mainRepository: MainRepository
) :
    BaseStateMachine<HostDetailsState, HostDetailsAction, HostDetailsEffect>(initialState = HostDetailsState()) {
    init {
        spec {
            inState<HostDetailsState> {
                on<HostDetailsAction.GetHostDetails> { action, state ->
                    updateEffect(HostDetailsEffect.ShowLoader)
                    val result = mainRepository.getScanDetails(action.hostId)
                    updateEffect(HostDetailsEffect.CancelLoader)
                    when (result) {
                        is Result.Success -> {
                            return@on state.override { HostDetailsState(details = result.data) }
                        }

                        is Result.Error -> {
                            when (result.exception) {
                                else -> {
                                    return@on state.override {
                                        HostDetailsState(
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
    }
}