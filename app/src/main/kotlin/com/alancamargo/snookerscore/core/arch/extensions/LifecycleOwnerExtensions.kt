package com.alancamargo.snookerscore.core.arch.extensions

import androidx.lifecycle.LifecycleOwner
import com.alancamargo.snookerscore.core.arch.viewmodel.ActionViewModel
import com.alancamargo.snookerscore.core.arch.viewmodel.UiAction
import com.alancamargo.snookerscore.core.arch.viewmodel.UiState
import com.alancamargo.snookerscore.core.arch.viewmodel.ViewModel

fun <S : UiState, A : UiAction> LifecycleOwner.observeState(
    viewModel: ViewModel<S, A>,
    handleState: (S) -> Unit
) {
    viewModel.state.observe(this) { state ->
        handleState(state)
    }
}

fun <S : UiState, A : UiAction> LifecycleOwner.observeAction(
    viewModel: ViewModel<S, A>,
    handleAction: (A) -> Unit
) {
    viewModel.action.observe(this) { action ->
        handleAction(action)
    }
}

fun <A : UiAction> LifecycleOwner.observeAction(
    viewModel: ActionViewModel<A>,
    handleAction: (A) -> Unit
) {
    viewModel.action.observe(this) { action ->
        handleAction(action)
    }
}