package com.alancamargo.snookerscore.features.match.ui.viewmodel.list

import androidx.lifecycle.viewModelScope
import com.alancamargo.snookerscore.core.arch.viewmodel.ViewModel
import com.alancamargo.snookerscore.core.data.log.Logger
import com.alancamargo.snookerscore.features.match.domain.usecase.GetMatchesUseCase
import com.alancamargo.snookerscore.features.match.ui.mapping.toUi
import com.alancamargo.snookerscore.features.match.ui.model.UiMatch
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
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
                .catch { throwable ->
                    logger.error(throwable)
                    sendAction { MatchListUiAction.ShowError }
                }.collect { matches ->
                    val uiMatches = matches.map { it.toUi() }
                    setState { state -> state.onMatchesReceived(uiMatches) }
                }
        }
    }

}
