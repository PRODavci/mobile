package com.mireascanner.features.main.presentation.host_details

import android.util.Log
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
                            Log.d("Tagdelete", HostDetailsState(details = result.data.hosts.find { it.id == action.hostId }).toString())
                            return@on state.override { HostDetailsState(details = result.data.hosts.find { it.id == action.hostId }) }
                        }

                        is Result.Error -> {
                            when (result.exception) {
                                else -> {
                                    Log.d("Tagdelete", result.exception.toString())
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