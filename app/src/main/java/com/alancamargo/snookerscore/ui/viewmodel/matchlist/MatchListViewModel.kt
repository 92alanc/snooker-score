package com.alancamargo.snookerscore.ui.viewmodel.matchlist

import androidx.lifecycle.viewModelScope
import com.alancamargo.snookerscore.core.arch.viewmodel.ViewModel
import com.alancamargo.snookerscore.core.log.Logger
import com.alancamargo.snookerscore.domain.usecase.match.GetMatchesUseCase
import com.alancamargo.snookerscore.ui.mapping.toUi
import com.alancamargo.snookerscore.ui.model.UiMatch
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class MatchListViewModel(
    private val getMatchesUseCase: GetMatchesUseCase,
    private val logger: Logger,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel<MatchListUiState, MatchListUiAction>(initialState = MatchListUiState()) {

    fun onNewMatchClicked() {
        sendAction { MatchListUiAction.OpenNewMatch }
    }

    fun onMatchClicked(match: UiMatch) {
        sendAction { MatchListUiAction.OpenMatchDetails(match) }
    }

    fun getMatches() {
        viewModelScope.launch {
            getMatchesUseCase().flowOn(dispatcher)
                .onStart {
                    sendAction { MatchListUiAction.ShowLoading }
                }.onCompletion {
                    sendAction { MatchListUiAction.HideLoading }
                }.catch { throwable ->
                    logger.error(throwable)
                    sendAction { MatchListUiAction.ShowError }
                }.collect { matches ->
                    val uiMatches = matches.map { it.toUi() }
                    setState { state -> state.onMatchesReceived(uiMatches) }
                }
        }
    }

}
