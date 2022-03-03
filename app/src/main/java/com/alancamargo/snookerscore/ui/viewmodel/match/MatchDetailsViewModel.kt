package com.alancamargo.snookerscore.ui.viewmodel.match

import androidx.lifecycle.viewModelScope
import com.alancamargo.snookerscore.core.arch.viewmodel.ActionViewModel
import com.alancamargo.snookerscore.core.log.Logger
import com.alancamargo.snookerscore.domain.usecase.match.DeleteMatchUseCase
import com.alancamargo.snookerscore.ui.mapping.toDomain
import com.alancamargo.snookerscore.ui.model.UiMatch
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class MatchDetailsViewModel(
    private val deleteMatchUseCase: DeleteMatchUseCase,
    private val logger: Logger,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ActionViewModel<MatchDetailsUiAction>() {

    fun onDeleteMatchClicked(match: UiMatch) {
        viewModelScope.launch {
            deleteMatchUseCase(match.toDomain()).flowOn(dispatcher)
                .onStart {
                    sendAction { MatchDetailsUiAction.ShowLoading }
                }.onCompletion {
                    sendAction { MatchDetailsUiAction.HideLoading }
                }.catch { throwable ->
                    logger.error(throwable)
                    sendAction { MatchDetailsUiAction.ShowError }
                }.collect {
                    sendAction { MatchDetailsUiAction.Finish }
                }
        }
    }

}
