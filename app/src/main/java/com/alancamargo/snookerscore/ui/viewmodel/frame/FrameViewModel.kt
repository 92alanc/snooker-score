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
import com.alancamargo.snookerscore.domain.usecase.player.DrawPlayerUseCase
import com.alancamargo.snookerscore.domain.usecase.player.GetWinningPlayerUseCase
import com.alancamargo.snookerscore.domain.usecase.playerstats.AddOrUpdatePlayerStatsUseCase
import com.alancamargo.snookerscore.domain.usecase.playerstats.GetPlayerStatsUseCase
import com.alancamargo.snookerscore.domain.usecase.playerstats.UpdatePlayerStatsWithMatchResultUseCase
import com.alancamargo.snookerscore.ui.mapping.toDomain
import com.alancamargo.snookerscore.ui.mapping.toUi
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
    private val logger: Logger,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel<FrameUiState, FrameUiAction>(initialState = FrameUiState()) {

    private var currentFrameIndex = 0
    private var currentFrame: UiFrame? = null
    private var currentPlayer: UiPlayer? = null
    private var player1: Player? = null
    private var player2: Player? = null
    private var lastFoul: Foul? = null

    init {
        frames.first().let { firstFrame ->
            currentFrame = firstFrame
            setState { state -> state.setCurrentFrame(firstFrame) }

            firstFrame.match.player1.toDomain().let { p1 ->
                firstFrame.match.player2.toDomain().let { p2 ->
                    player1 = p1
                    player2 = p2

                    val playerDrawn = useCases.playerUseCases.drawPlayerUseCase(p1, p2).toUi()
                    currentPlayer = playerDrawn
                    setState { state -> state.setCurrentPlayer(playerDrawn) }
                }
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

            val breakValue = breakCalculator.getPoints()
            setState { state -> state.onBreakUpdated(breakValue) }
            setState { state -> state.onDisableUndoLastPottedBallButton() }
        }
    }

    fun onObjectBallFoulClicked() {
        // TODO
    }

    fun onFoul(foul: Foul) {
        takeFrameAndPlayerIfNotNull { frame, player ->
            lastFoul = foul
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
        lastFoul?.let { foul ->
            takeFrameAndPlayerIfNotNull { frame, player ->
                val penaltyValue = useCases.getPenaltyValueUseCase(foul)

                if (player == frame.match.player1) {
                    frame.player2Score -= penaltyValue
                    setState { state -> state.onPlayer2ScoreUpdated(frame.player2Score) }
                } else if (player == frame.match.player2) {
                    frame.player1Score -= penaltyValue
                    setState { state -> state.onPlayer1ScoreUpdated(frame.player1Score) }
                }

                lastFoul = null
                setState { state -> state.onDisableUndoLastFoulButton() }
            }
        }
    }

    fun onEndTurnClicked() {
        sendAction { FrameUiAction.ShowEndTurnConfirmation }
    }

    fun onEndTurnConfirmed() {
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
            lastFoul = null
        }
    }

    fun onEndFrameClicked() {
        sendAction { FrameUiAction.ShowEndFrameConfirmation }
    }

    fun onEndFrameConfirmed() {
        takeFrameAndPlayerIfNotNull { frame, _ ->
            onEndTurnConfirmed()

            currentFrameIndex = frames.indexOf(frame) + 1

            if (currentFrameIndex <= frames.lastIndex) {
                currentFrame = frames[currentFrameIndex]
                setState { state -> state.setCurrentFrame(frames[currentFrameIndex]) }
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

    private suspend fun addOrUpdateFrame(frame: UiFrame) {
        useCases.addOrUpdateFrameUseCase(frame.toDomain()).handleDefaultActions().collect {
            setState { state -> state.setCurrentFrame(frame) }
        }
    }

    private fun endMatch() {
        val domainFrames = frames.map { it.toDomain() }

        useCases.playerUseCases.getWinningPlayerUseCase(domainFrames)?.let { winner ->
            viewModelScope.launch {
                useCases.playerStatsUseCases.getPlayerStatsUseCase(winner).handleDefaultActions()
                    .collect { currentWinnerStats ->
                        useCases.playerStatsUseCases.updatePlayerStatsWithMatchResultUseCase(
                            currentWinnerStats
                        ).handleDefaultActions()
                            .collect()
                    }
            }
        } ?: run {
            sendAction { FrameUiAction.ShowError }
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
        val playerUseCases: PlayerUseCases,
        val playerStatsUseCases: PlayerStatsUseCases,
        val addOrUpdateFrameUseCase: AddOrUpdateFrameUseCase,
        val getPenaltyValueUseCase: GetPenaltyValueUseCase,
        val deleteMatchUseCase: DeleteMatchUseCase
    )

    class PlayerUseCases(
        val drawPlayerUseCase: DrawPlayerUseCase,
        val getWinningPlayerUseCase: GetWinningPlayerUseCase
    )

    class PlayerStatsUseCases(
        val getPlayerStatsUseCase: GetPlayerStatsUseCase,
        val addOrUpdatePlayerStatsUseCase: AddOrUpdatePlayerStatsUseCase,
        val updatePlayerStatsWithMatchResultUseCase: UpdatePlayerStatsWithMatchResultUseCase
    )

}
