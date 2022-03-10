package com.alancamargo.snookerscore.ui.viewmodel.frame

import androidx.lifecycle.viewModelScope
import com.alancamargo.snookerscore.core.arch.viewmodel.ViewModel
import com.alancamargo.snookerscore.core.log.Logger
import com.alancamargo.snookerscore.domain.model.Ball
import com.alancamargo.snookerscore.domain.model.Foul
import com.alancamargo.snookerscore.domain.model.Player
import com.alancamargo.snookerscore.domain.tools.BreakCalculator
import com.alancamargo.snookerscore.domain.usecase.foul.GetPenaltyValueUseCase
import com.alancamargo.snookerscore.domain.usecase.frame.AddOrUpdateFrameUseCase
import com.alancamargo.snookerscore.domain.usecase.match.DeleteMatchUseCase
import com.alancamargo.snookerscore.domain.usecase.player.GetWinningPlayerUseCase
import com.alancamargo.snookerscore.domain.usecase.playerstats.AddOrUpdatePlayerStatsUseCase
import com.alancamargo.snookerscore.domain.usecase.playerstats.GetPlayerStatsUseCase
import com.alancamargo.snookerscore.domain.usecase.playerstats.UpdatePlayerStatsWithMatchResultUseCase
import com.alancamargo.snookerscore.ui.mapping.toDomain
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
import java.util.Stack

class FrameViewModel(
    private val frames: List<UiFrame>,
    private val useCases: UseCases,
    private val breakCalculator: BreakCalculator,
    private val logger: Logger,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel<FrameUiState, FrameUiAction>(initialState = FrameUiState()) {

    private val pottedBalls = Stack<Ball>()
    private val fouls = Stack<Foul>()

    private var frameIndex = 0
    private var currentFrame: UiFrame? = null
    private var currentPlayer: UiPlayer? = null
    private var player1: Player? = null
    private var player2: Player? = null

    init {
        val frame = frames.first { !it.isFinished }
        val match = frame.match
        setState { state -> state.setCurrentFrame(frame) }

        sendAction {
            FrameUiAction.ShowStartingPlayerPrompt(
                player1 = match.player1,
                player2 = match.player2
            )
        }
    }

    fun onStartingPlayerSelected(player: UiPlayer) {
        val frame = currentFrame ?: frames.first { !it.isFinished }.also { currentFrame = it }

        frame.match.player1.toDomain().let { p1 ->
            frame.match.player2.toDomain().let { p2 ->
                player1 = p1
                player2 = p2

                currentPlayer = player
                setState { state -> state.setCurrentPlayer(player) }
            }
        }
    }

    fun onBallPotted(ball: Ball) {
        takeFrameAndPlayerIfNotNull { frame, player ->
            breakCalculator.potBall(ball)

            if (player == frame.match.player1) {
                frame.player1Score += ball.value
                setState { state -> state.onPlayer1ScoreUpdated(frame.player1Score) }
            } else if (player == frame.match.player2) {
                frame.player2Score += ball.value
                setState { state -> state.onPlayer2ScoreUpdated(frame.player2Score) }
            }

            pottedBalls.push(ball)
            setState { state -> state.onEnableUndoLastPottedBallButton() }

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

            pottedBalls.pop()
            val breakValue = breakCalculator.getPoints()
            setState { state -> state.onBreakUpdated(breakValue) }

            if (pottedBalls.isEmpty()) {
                setState { state -> state.onDisableUndoLastPottedBallButton() }
            }
        }
    }

    fun onObjectBallFoulClicked() {
        sendAction { FrameUiAction.ShowObjectBalls }
    }

    fun onFoul(foul: Foul) {
        takeFrameAndPlayerIfNotNull { frame, player ->
            fouls.push(foul)
            val penaltyValue = useCases.getPenaltyValueUseCase(foul)

            if (player == frame.match.player1) {
                frame.player2Score += penaltyValue
                setState { state -> state.onPlayer2ScoreUpdated(frame.player2Score) }
            } else if (player == frame.match.player2) {
                frame.player1Score += penaltyValue
                setState { state -> state.onPlayer1ScoreUpdated(frame.player1Score) }
            }

            setState { state -> state.onEnableUndoLastFoulButton() }
        }
    }

    fun onUndoLastFoulClicked() {
        fouls.pop().let { foul ->
            takeFrameAndPlayerIfNotNull { frame, player ->
                val penaltyValue = useCases.getPenaltyValueUseCase(foul)

                if (player == frame.match.player1) {
                    frame.player2Score -= penaltyValue
                    setState { state -> state.onPlayer2ScoreUpdated(frame.player2Score) }
                } else if (player == frame.match.player2) {
                    frame.player1Score -= penaltyValue
                    setState { state -> state.onPlayer1ScoreUpdated(frame.player1Score) }
                }

                if (fouls.isEmpty()) {
                    setState { state -> state.onDisableUndoLastFoulButton() }
                }
            }
        }
    }

    fun onEndTurnClicked(isEndingFrame: Boolean = false) {
        takeFrameAndPlayerIfNotNull { frame, player ->
            val breakPoints = breakCalculator.getPoints()

            if (player == frame.match.player1) {
                if (breakPoints > frame.player1HighestBreak) {
                    frame.player1HighestBreak = breakPoints
                }

                if (!isEndingFrame) {
                    currentPlayer = frame.match.player2
                    setState { state -> state.setCurrentPlayer(frame.match.player2) }
                }
            } else {
                if (breakPoints > frame.player2HighestBreak) {
                    frame.player2HighestBreak = breakPoints
                }

                if (!isEndingFrame) {
                    currentPlayer = frame.match.player1
                    setState { state -> state.setCurrentPlayer(frame.match.player1) }
                }
            }

            viewModelScope.launch {
                addOrUpdatePlayerStats(frame, player)
                addOrUpdateFrame(isEndingFrame)
            }

            breakCalculator.clear()
            pottedBalls.clear()
            fouls.clear()
            setState { state -> state.onDisableUndoLastPottedBallButton() }
            setState { state -> state.onDisableUndoLastFoulButton() }
            setState { state -> state.onBreakUpdated(breakCalculator.getPoints()) }
        }
    }

    fun onEndFrameClicked() {
        sendAction { FrameUiAction.ShowEndFrameConfirmation }
    }

    fun onEndFrameConfirmed() {
        takeFrameAndPlayerIfNotNull { frame, _ ->
            onEndTurnClicked(isEndingFrame = true)

            if (frameIndex <= frames.lastIndex - 1) {
                frameIndex++
                setState { state -> state.setCurrentFrame(frames[frameIndex]) }
                sendAction {
                    FrameUiAction.ShowStartingPlayerPrompt(
                        frame.match.player1,
                        frame.match.player2
                    )
                }
            } else {
                endMatch()
                sendAction { FrameUiAction.OpenMatchSummary(frame.match) }
            }
        }
    }

    fun onForfeitMatchClicked() {
        sendAction { FrameUiAction.ShowForfeitMatchConfirmation }
    }

    fun onForfeitMatchConfirmed() {
        takeFrameAndPlayerIfNotNull { frame, _ ->
            viewModelScope.launch {
                useCases.deleteMatchUseCase(frame.match.toDomain()).handleDefaultActions()
                    .collect {
                        sendAction { FrameUiAction.OpenMain }
                    }
            }
        }
    }

    private suspend fun addOrUpdatePlayerStats(frame: UiFrame, player: UiPlayer) {
        useCases.playerStatsUseCases.getPlayerStatsUseCase(player.toDomain()).handleDefaultActions()
            .collect { currentPlayerStats ->
                useCases.playerStatsUseCases.addOrUpdatePlayerStatsUseCase(
                    currentPlayerStats,
                    frame.toDomain(),
                    player.toDomain()
                ).handleDefaultActions().collect()
            }
    }

    private suspend fun addOrUpdateFrame(isEndingFrame: Boolean) {
        if (isEndingFrame) {
            frames.find { it == currentFrame }?.isFinished = true
            currentFrame = frames[frameIndex]
        }

        frames.forEach { frame ->
            useCases.addOrUpdateFrameUseCase(frame.toDomain()).handleDefaultActions().collect()
        }
    }

    private fun endMatch() {
        val domainFrames = frames.map { it.toDomain() }

        useCases.getWinningPlayerUseCase(domainFrames).let { winner ->
            viewModelScope.launch {
                useCases.playerStatsUseCases.getPlayerStatsUseCase(winner).handleDefaultActions()
                    .collect { currentWinnerStats ->
                        useCases.playerStatsUseCases.updatePlayerStatsWithMatchResultUseCase(
                            currentWinnerStats
                        ).handleDefaultActions()
                            .collect()
                    }
            }
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
            }.catch { throwable ->
                logger.error(throwable)
                sendAction { FrameUiAction.ShowError }
            }
    }

    class UseCases(
        val getWinningPlayerUseCase: GetWinningPlayerUseCase,
        val playerStatsUseCases: PlayerStatsUseCases,
        val addOrUpdateFrameUseCase: AddOrUpdateFrameUseCase,
        val getPenaltyValueUseCase: GetPenaltyValueUseCase,
        val deleteMatchUseCase: DeleteMatchUseCase
    )

    class PlayerStatsUseCases(
        val getPlayerStatsUseCase: GetPlayerStatsUseCase,
        val addOrUpdatePlayerStatsUseCase: AddOrUpdatePlayerStatsUseCase,
        val updatePlayerStatsWithMatchResultUseCase: UpdatePlayerStatsWithMatchResultUseCase
    )

}
