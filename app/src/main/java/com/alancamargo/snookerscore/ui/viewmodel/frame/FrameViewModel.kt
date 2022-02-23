package com.alancamargo.snookerscore.ui.viewmodel.frame

import androidx.lifecycle.viewModelScope
import com.alancamargo.snookerscore.core.arch.viewmodel.ViewModel
import com.alancamargo.snookerscore.domain.model.Foul
import com.alancamargo.snookerscore.domain.model.Player
import com.alancamargo.snookerscore.domain.model.PlayerStats
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
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.zip
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
    private var player1Stats: PlayerStats? = null
    private var player2Stats: PlayerStats? = null

    init {
        frames.first().let { firstFrame ->
            currentFrame = firstFrame
            setState { state -> state.setCurrentFrame(firstFrame) }

            firstFrame.match.player1.toDomain().let { p1 ->
                firstFrame.match.player2.toDomain().let { p2 ->
                    player1 = p1
                    player2 = p2

                    getPlayerStats(p1, p2)

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
            } else if (player == frame.match.player2) {
                frame.player2Score += domainBall.value
            }

            addOrUpdateFrame(frame)
            setState { state -> state.onEnableLastPottedBallButton() }

            val breakValue = breakCalculator.getPoints()
            setState { state -> state.onBreakUpdated(breakValue) }
            // TODO: add test
        }
    }

    fun onUndoLastPottedBallClicked() {
        val lastPottedBall = breakCalculator.undoLastPottedBall()

        takeFrameAndPlayerIfNotNull { frame, player ->
            if (player == frame.match.player1) {
                frame.player1Score -= lastPottedBall.value
            } else if (player == frame.match.player2) {
                frame.player2Score -= lastPottedBall.value
            }

            addOrUpdateFrame(frame)
        }
    }

    fun onFoul(foul: Foul) {
        takeFrameAndPlayerIfNotNull { frame, player ->
            val penaltyValue = useCases.getPenaltyValueUseCase(foul)

            if (player == frame.match.player1) {
                frame.player2Score += penaltyValue
            } else if (player == frame.match.player2) {
                frame.player1Score += penaltyValue
            }

            onEndTurnClicked()
        }
    }

    fun onEndTurnClicked() {
        takeFrameAndPlayerIfNotNull { frame, player ->
            val breakPoints = breakCalculator.getPoints()

            if (player == frame.match.player1) {
                if (breakPoints > frame.player1HighestBreak) {
                    frame.player1HighestBreak = breakPoints
                }

                setState { state -> state.setCurrentPlayer(frame.match.player2) }
            } else if (player == frame.match.player2) {
                if (breakPoints > frame.player2HighestBreak) {
                    frame.player2HighestBreak = breakPoints
                }

                setState { state -> state.setCurrentPlayer(frame.match.player1) }
            }

            addOrUpdateFrame(frame)
            breakCalculator.clear()
            setState { state -> state.onDisableLastPottedBallButton() }
        }
    }

    fun onEndFrameClicked() {
        onEndTurnClicked()
        val newFrameIndex = ++currentFrameIndex

        // TODO: update highest scores and breaks

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
                useCases.deleteMatchUseCase(frame.match.toDomain()).flowOn(dispatcher)
                    .onStart {
                        sendAction { FrameUiAction.ShowLoading }
                    }.onCompletion {
                        sendAction { FrameUiAction.HideLoading }
                    }.catch {
                        sendAction { FrameUiAction.ShowError }
                    }.collect {
                        sendAction { FrameUiAction.OpenMain }
                    }
            }
        }
    }

    private fun addOrUpdateFrame(frame: UiFrame) {
        viewModelScope.launch {
            useCases.addOrUpdateFrameUseCase(frame.toDomain()).flowOn(dispatcher)
                .onStart {
                    sendAction { FrameUiAction.ShowLoading }
                }.onCompletion {
                    sendAction { FrameUiAction.HideLoading }
                }.catch {
                    sendAction { FrameUiAction.ShowError }
                }.collect {
                    setState { state -> state.setCurrentFrame(frame) }
                }
        }
    }

    private fun getPlayerStats(player1: Player, player2: Player) {
        viewModelScope.launch {
            useCases.getPlayerStatsUseCase(player1)
                .zip(useCases.getPlayerStatsUseCase(player2)) { player1Stats, player2Stats ->
                    player1Stats to player2Stats
                }.flowOn(dispatcher)
                .onStart {
                    sendAction { FrameUiAction.ShowLoading }
                }.onCompletion {
                    sendAction { FrameUiAction.HideLoading }
                }.catch {
                    sendAction { FrameUiAction.ShowError }
                }.collect {
                    player1Stats = it.first
                    player2Stats = it.second
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

    class UseCases(
        val drawPlayerUseCase: DrawPlayerUseCase,
        val addOrUpdateFrameUseCase: AddOrUpdateFrameUseCase,
        val getPenaltyValueUseCase: GetPenaltyValueUseCase,
        val deleteMatchUseCase: DeleteMatchUseCase,
        val getPlayerStatsUseCase: GetPlayerStatsUseCase,
        val addOrUpdatePlayerStatsUseCase: AddOrUpdatePlayerStatsUseCase
    )

}
