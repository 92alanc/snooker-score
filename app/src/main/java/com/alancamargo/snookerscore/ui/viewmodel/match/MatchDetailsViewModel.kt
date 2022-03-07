package com.alancamargo.snookerscore.ui.viewmodel.match

import androidx.lifecycle.viewModelScope
import com.alancamargo.snookerscore.core.arch.viewmodel.ViewModel
import com.alancamargo.snookerscore.core.log.Logger
import com.alancamargo.snookerscore.domain.model.Frame
import com.alancamargo.snookerscore.domain.usecase.frame.GetFramesUseCase
import com.alancamargo.snookerscore.domain.usecase.match.DeleteMatchUseCase
import com.alancamargo.snookerscore.domain.usecase.player.GetWinningPlayerUseCase
import com.alancamargo.snookerscore.ui.mapping.toDomain
import com.alancamargo.snookerscore.ui.mapping.toUi
import com.alancamargo.snookerscore.ui.model.UiFrame
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
    private val getFramesUseCase: GetFramesUseCase,
    private val deleteMatchUseCase: DeleteMatchUseCase,
    private val getWinningPlayerUseCase: GetWinningPlayerUseCase,
    private val logger: Logger,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel<MatchDetailsUiState, MatchDetailsUiAction>(initialState = MatchDetailsUiState()) {

    private var match: UiMatch? = null
    private var frames = mutableListOf<UiFrame>()

    fun getMatchDetails(match: UiMatch) {
        this.match = match

        viewModelScope.launch {
            getFramesUseCase(match.toDomain()).flowOn(dispatcher)
                .onStart {
                    sendAction { MatchDetailsUiAction.ShowLoading }
                }.onCompletion {
                    sendAction { MatchDetailsUiAction.HideLoading }
                }.catch { throwable ->
                    logger.error(throwable)
                    sendAction { MatchDetailsUiAction.ShowError }
                }.collect { result ->
                    frames = result.map { it.toUi() }.toMutableList()
                    if (frames.isNotEmpty()) {
                        setState { state -> state.onFramesReceived(frames) }
                    }

                    val winner = getWinningPlayerUseCase(result)
                    setState { state -> state.onWinnerSet(winner?.toUi()) }

                    if (frames.any { !it.isFinished }) {
                        setState { state -> state.onEnableResumeMatchButton() }
                    }
                }
        }
    }

    fun onResumeMatchClicked() {
        match?.let {
            if (frames.isEmpty()) {
                repeat(it.numberOfFrames) { index ->
                    val frame = Frame(
                        positionInMatch = index + 1,
                        match = it.toDomain()
                    ).toUi()

                    frames.add(frame)
                }
            }

            sendAction { MatchDetailsUiAction.ResumeMatch(frames) }
        }
    }

    fun onDeleteMatchClicked() {
        sendAction { MatchDetailsUiAction.ShowDeleteMatchConfirmation }
    }

    fun onDeleteMatchConfirmed(match: UiMatch) {
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
