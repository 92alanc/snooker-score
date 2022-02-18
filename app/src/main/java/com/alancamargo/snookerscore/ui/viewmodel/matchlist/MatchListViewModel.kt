package com.alancamargo.snookerscore.ui.viewmodel.matchlist

import androidx.lifecycle.viewModelScope
import com.alancamargo.snookerscore.core.arch.viewmodel.ViewModel
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
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel<MatchListUiState, MatchListUiAction>(initialState = MatchListUiState()) {

    init {
        getMatches()
    }

    fun onNewMatchClicked() {
        sendAction { MatchListUiAction.OpenNewMatch }
    }

    fun onMatchClicked(match: UiMatch) {
        sendAction { MatchListUiAction.OpenMatchDetails(match) }
    }

    private fun getMatches() {
        viewModelScope.launch {
            getMatchesUseCase().flowOn(dispatcher)
                .onStart {
                    sendAction { MatchListUiAction.ShowLoading }
                }.onCompletion {
                    sendAction { MatchListUiAction.HideLoading }
                }.catch {
                    sendAction { MatchListUiAction.ShowError }
                }.collect { matches ->
                    val uiMatches = matches.map { it.toUi() }
                    setState { state -> state.onMatchesReceived(uiMatches) }
                }
        }
    }

}
