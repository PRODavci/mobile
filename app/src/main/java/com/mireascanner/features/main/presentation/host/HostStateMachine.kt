package com.mireascanner.features.main.presentation.host

import com.mireascanner.common.utils.BaseStateMachine

class HostStateMachine : BaseStateMachine<HostState, HostAction, HostEffect>(initialState = HostState.Loading) {
}