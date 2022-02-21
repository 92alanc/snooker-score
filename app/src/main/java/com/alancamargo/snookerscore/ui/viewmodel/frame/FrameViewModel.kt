package com.alancamargo.snookerscore.ui.viewmodel.frame

import androidx.lifecycle.viewModelScope
import com.alancamargo.snookerscore.core.arch.viewmodel.ViewModel
import com.alancamargo.snookerscore.domain.model.Foul
import com.alancamargo.snookerscore.domain.tools.BreakCalculator
import com.alancamargo.snookerscore.domain.usecase.foul.GetPenaltyValueUseCase
import com.alancamargo.snookerscore.domain.usecase.frame.AddOrUpdateFrameUseCase
import com.alancamargo.snookerscore.domain.usecase.player.DrawPlayerUseCase
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
import kotlinx.coroutines.launch

class FrameViewModel(
    private val frames: List<UiFrame>,
    private val drawPlayerUseCase: DrawPlayerUseCase,
    private val addOrUpdateFrameUseCase: AddOrUpdateFrameUseCase,
    private val breakCalculator: BreakCalculator,
    private val getPenaltyValueUseCase: GetPenaltyValueUseCase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel<FrameUiState, FrameUiAction>(initialState = FrameUiState()) {

    private var currentFrameIndex = 0
    private var currentFrame: UiFrame? = null
    private var currentPlayer: UiPlayer? = null

    init {
        frames.first().let { firstFrame ->
            currentFrame = firstFrame
            setState { state -> state.setCurrentFrame(firstFrame) }

            val player1 = firstFrame.match.player1.toDomain()
            val player2 = firstFrame.match.player2.toDomain()
            val playerDrawn = drawPlayerUseCase(player1, player2).toUi()
            currentPlayer = playerDrawn
            setState { state -> state.setCurrentPlayer(playerDrawn) }
        }
    }

    fun onBallPotted(ball: UiBall) {
        takeFrameAndPlayerIfNotNull { frame, player ->
            val domainBall = ball.toDomain()
            breakCalculator.potBall(domainBall)

            if (player == frame.match.player1) {
                frame.player1Score += domainBall.value
            } else {
                frame.player2Score += domainBall.value
            }

            addOrUpdateFrame(frame)
        }
    }

    fun onUndoLastPottedBallClicked() {
        val lastPottedBall = breakCalculator.undoLastPottedBall()

        takeFrameAndPlayerIfNotNull { frame, player ->
            if (player == frame.match.player1) {
                frame.player1Score -= lastPottedBall.value
            } else {
                frame.player2Score -= lastPottedBall.value
            }

            addOrUpdateFrame(frame)
        }
    }

    fun onFoul(foul: Foul) {
        takeFrameAndPlayerIfNotNull { frame, player ->
            val penaltyValue = getPenaltyValueUseCase(foul)

            if (player == frame.match.player1) {
                frame.player2Score += penaltyValue
            } else {
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
            } else {
                if (breakPoints > frame.player2HighestBreak) {
                    frame.player2HighestBreak = breakPoints
                }

                setState { state -> state.setCurrentPlayer(frame.match.player1) }
            }

            addOrUpdateFrame(frame)
            breakCalculator.clear()
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

    private fun addOrUpdateFrame(frame: UiFrame) {
        viewModelScope.launch {
            addOrUpdateFrameUseCase(frame.toDomain()).flowOn(dispatcher)
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

    private fun takeFrameAndPlayerIfNotNull(block: (UiFrame, UiPlayer) -> Unit) {
        currentFrame?.let { frame ->
            currentPlayer?.let { player ->
                block(frame, player)
            }
        }
    }

}
