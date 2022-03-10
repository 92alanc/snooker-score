package com.alancamargo.snookerscore.ui.viewmodel.match

import androidx.lifecycle.viewModelScope
import com.alancamargo.snookerscore.core.arch.viewmodel.ViewModel
import com.alancamargo.snookerscore.core.log.Logger
import com.alancamargo.snookerscore.domain.usecase.frame.GetFramesUseCase
import com.alancamargo.snookerscore.domain.usecase.match.DeleteMatchUseCase
import com.alancamargo.snookerscore.domain.usecase.match.GetMatchSummaryUseCase
import com.alancamargo.snookerscore.ui.mapping.toDomain
import com.alancamargo.snookerscore.ui.mapping.toUi
import com.alancamargo.snookerscore.ui.model.UiFrame
import com.alancamargo.snookerscore.ui.model.UiMatch
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class MatchDetailsViewModel(
    private val getFramesUseCase: GetFramesUseCase,
    private val deleteMatchUseCase: DeleteMatchUseCase,
    private val getMatchSummaryUseCase: GetMatchSummaryUseCase,
    private val logger: Logger,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel<MatchDetailsUiState, MatchDetailsUiAction>(initialState = MatchDetailsUiState()) {

    private var match: UiMatch? = null
    private var frames = mutableListOf<UiFrame>()

    fun getMatchDetails(match: UiMatch) {
        this.match = match

        viewModelScope.launch {
            getFramesUseCase(match.toDomain()).doOnCollect { result ->
                frames = result.map { it.toUi() }.toMutableList()
                if (frames.isNotEmpty()) {
                    setState { state -> state.onFramesReceived(frames) }
                }

                val winner = getMatchSummaryUseCase(result).winner
                setState { state -> state.onWinnerSet(winner.toUi()) }
            }
        }
    }

    fun onViewSummaryClicked() {
        sendAction { MatchDetailsUiAction.ViewSummary(frames) }
    }

    fun onDeleteMatchClicked() {
        sendAction { MatchDetailsUiAction.ShowDeleteMatchConfirmation }
    }

    fun onDeleteMatchConfirmed(match: UiMatch) {
        viewModelScope.launch {
            deleteMatchUseCase(match.toDomain()).doOnCollect {
                sendAction { MatchDetailsUiAction.Finish }
            }
        }
    }

    private suspend fun <T> Flow<T>.doOnCollect(onCollect: (T) -> Unit) {
        flowOn(dispatcher).catch { throwable ->
            logger.error(throwable)
            sendAction { MatchDetailsUiAction.ShowError }
        }.collect {
            onCollect(it)
        }
    }

}
