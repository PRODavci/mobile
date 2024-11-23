package com.mireascanner.features.main.main

import com.mireascanner.common.main.domain.MainRepository
import com.mireascanner.common.utils.BaseStateMachine
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class MainStateMachine @Inject constructor(
    private val mainRepository: MainRepository
) :
    BaseStateMachine<MainState, MainAction, MainEffect>(initialState = MainState.Loading) {

    init {
        spec {
            inState<MainState.Content> {

            }

            inState<MainState.Loading> {

            }
        }
    }

}