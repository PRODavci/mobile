package com.mireascanner.features.main.presentation.host_details

import android.util.Log
import com.mireascanner.R
import com.mireascanner.common.exceptions.HostDetailsNotFoundException
import com.mireascanner.common.main.data.remote.model.DetailedHostResponse
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
                    val result = mainRepository.getScanDetails(action.scanId)
                    updateEffect(HostDetailsEffect.CancelLoader)
                    when (result) {
                        is Result.Success -> {
                            Log.d("Tagdelete", HostDetailsState(details = result.data.hosts.find { it.id == action.hostId }).toString())
                            var data = result.data.hosts.find { it.id == action.hostId }
                            if(data == null){
                                data = DetailedHostResponse(0, "", emptyList())
                            }
                            return@on state.override { HostDetailsState(details = data) }
                        }

                        is Result.Error -> {
                            when (result.exception) {
                                is HostDetailsNotFoundException -> {
                                    return@on state.override {
                                        HostDetailsState(
                                            error = UIText.StringResource(
                                                R.string.text_services_not_found
                                            )
                                        )
                                    }
                                }
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