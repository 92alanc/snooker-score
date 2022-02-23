package com.alancamargo.snookerscore.ui.viewmodel.frame

import androidx.lifecycle.viewModelScope
import com.alancamargo.snookerscore.core.arch.viewmodel.ViewModel
import com.alancamargo.snookerscore.domain.model.Foul
import com.alancamargo.snookerscore.domain.model.Player
import com.alancamargo.snookerscore.domain.tools.BreakCalculator
import com.alancamargo.snookerscore.domain.usecase.foul.GetPenaltyValueUseCase
import com.alancamargo.snookerscore.domain.usecase.frame.AddOrUpdateFrameUseCase
import com.alancamargo.snookerscore.domain.usecase.match.DeleteMatchUseCase
import com.alancamargo.snookerscore.domain.usecase.player.DrawPlayerUseCase
import com.alancamargo.snookerscore.domain.usecase.playerstats.AddOrUpdatePlayerStatsUseCase
import com.alancamargo.snookerscore.domain.usecase.playerstats.GetPlayerStatsUseCase
import com.alancamargo.snookerscore.ui.mapping.toDomain
import com.alancamargo.snookerscore.ui.mapping.toUi
import com.alancamargo.snookerscore.ui.model.UiBall
import com.alancamargo.snookerscore.ui.model.UiFrame
import com.alancamargo.snookerscore.ui.model.UiPlayer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class FrameViewModel(
    private val frames: List<UiFrame>,
    private val useCases: UseCases,
    private val breakCalculator: BreakCalculator,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel<FrameUiState, FrameUiAction>(initialState = FrameUiState()) {

    private var currentFrameIndex = 0
    private var currentFrame: UiFrame? = null
    private var currentPlayer: UiPlayer? = null
    private var player1: Player? = null
    private var player2: Player? = null

    init {
        frames.first().let { firstFrame ->
            currentFrame = firstFrame
            setState { state -> state.setCurrentFrame(firstFrame) }

            firstFrame.match.player1.toDomain().let { p1 ->
                firstFrame.match.player2.toDomain().let { p2 ->
                    player1 = p1
                    player2 = p2

                    val playerDrawn = useCases.drawPlayerUseCase(p1, p2).toUi()
                    currentPlayer = playerDrawn
                    setState { state -> state.setCurrentPlayer(playerDrawn) }
                }
            }
        }
    }

    fun onBallPotted(ball: UiBall) {
        takeFrameAndPlayerIfNotNull { frame, player ->
            val domainBall = ball.toDomain()
            breakCalculator.potBall(domainBall)

            if (player == frame.match.player1) {
                frame.player1Score += domainBall.value
                setState { state -> state.onPlayer1ScoreUpdated(frame.player1Score) }
            } else if (player == frame.match.player2) {
                frame.player2Score += domainBall.value
                setState { state -> state.onPlayer2ScoreUpdated(frame.player2Score) }
            }

            setState { state -> state.onEnableLastPottedBallButton() }

            val breakValue = breakCalculator.getPoints()
            setState { state -> state.onBreakUpdated(breakValue) }
        }
    }

    fun onUndoLastPottedBallClicked() {
        val lastPottedBall = breakCalculator.undoLastPottedBall()

        takeFrameAndPlayerIfNotNull { frame, player ->
            if (player == frame.match.player1) {
                frame.player1Score -= lastPottedBall.value
                setState { state -> state.onPlayer1ScoreUpdated(frame.player1Score) }
            } else if (player == frame.match.player2) {
                frame.player2Score -= lastPottedBall.value
                setState { state -> state.onPlayer2ScoreUpdated(frame.player2Score) }
            }

            val breakValue = breakCalculator.getPoints()
            setState { state -> state.onBreakUpdated(breakValue) }
        }
    }

    fun onFoul(foul: Foul) {
        takeFrameAndPlayerIfNotNull { frame, player ->
            val penaltyValue = useCases.getPenaltyValueUseCase(foul)

            if (player == frame.match.player1) {
                frame.player2Score += penaltyValue
                setState { state -> state.onPlayer2ScoreUpdated(frame.player2Score) }
            } else if (player == frame.match.player2) {
                frame.player1Score += penaltyValue
                setState { state -> state.onPlayer1ScoreUpdated(frame.player1Score) }
            }

            onEndTurnClicked()
        }
    }

    fun onEndTurnClicked() {
        takeFrameAndPlayerIfNotNull { frame, player ->
            if (player == frame.match.player1) {
                setState { state -> state.setCurrentPlayer(frame.match.player2) }
            } else {
                setState { state -> state.setCurrentPlayer(frame.match.player1) }
            }

            viewModelScope.launch {
                addOrUpdatePlayerStats(frame, player)
                addOrUpdateFrame(frame)
            }

            breakCalculator.clear()
            setState { state -> state.onDisableLastPottedBallButton() }
        }
    }

    fun onEndFrameClicked() {
        onEndTurnClicked()
        val newFrameIndex = ++currentFrameIndex

        if (newFrameIndex <= frames.lastIndex) {
            currentFrame = frames[currentFrameIndex]
            setState { state -> state.setCurrentFrame(frames[currentFrameIndex]) }
        } else {
            currentFrame?.let { frame ->
                sendAction { FrameUiAction.OpenMatchSummary(frame.match) }
            }
        }
    }

    fun onForfeitMatchClicked() {
        takeFrameAndPlayerIfNotNull { frame, _ ->
            viewModelScope.launch {
                useCases.deleteMatchUseCase(frame.match.toDomain()).handleDefaultActions()
                    .collect {
                        sendAction { FrameUiAction.OpenMain }
                    }
            }
        }
    }

    private suspend fun addOrUpdatePlayerStats(
        frame: UiFrame, player: UiPlayer
    ) {
        useCases.getPlayerStatsUseCase(player.toDomain()).handleDefaultActions()
            .collect { currentPlayerStats ->
                useCases.addOrUpdatePlayerStatsUseCase(
                    currentPlayerStats,
                    frame.toDomain(),
                    player.toDomain()
                ).handleDefaultActions().collect()
            }
    }

    private suspend fun addOrUpdateFrame(frame: UiFrame) {
        useCases.addOrUpdateFrameUseCase(frame.toDomain()).handleDefaultActions().collect {
            setState { state -> state.setCurrentFrame(frame) }
        }
    }

    private fun takeFrameAndPlayerIfNotNull(block: (UiFrame, UiPlayer) -> Unit) {
        currentFrame?.let { frame ->
            currentPlayer?.let { player ->
                block(frame, player)
            }
        }
    }

    private fun <T> Flow<T>.handleDefaultActions(): Flow<T> {
        return this.flowOn(dispatcher)
            .onStart {
                sendAction { FrameUiAction.ShowLoading }
            }.onCompletion {
                sendAction { FrameUiAction.HideLoading }
            }.catch {
                sendAction { FrameUiAction.ShowError }
            }
    }

    class UseCases(
        val drawPlayerUseCase: DrawPlayerUseCase,
        val addOrUpdateFrameUseCase: AddOrUpdateFrameUseCase,
        val getPenaltyValueUseCase: GetPenaltyValueUseCase,
        val deleteMatchUseCase: DeleteMatchUseCase,
        val getPlayerStatsUseCase: GetPlayerStatsUseCase,
        val addOrUpdatePlayerStatsUseCase: AddOrUpdatePlayerStatsUseCase
    )

}
