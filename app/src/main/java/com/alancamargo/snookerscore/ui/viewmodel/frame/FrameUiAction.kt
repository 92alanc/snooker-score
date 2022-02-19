package com.alancamargo.snookerscore.ui.viewmodel.frame

import com.alancamargo.snookerscore.core.arch.viewmodel.UiAction
import com.alancamargo.snookerscore.ui.model.UiMatch

sealed class FrameUiAction : UiAction {

    object ShowLoading : FrameUiAction()

    object HideLoading : FrameUiAction()

    object ShowError : FrameUiAction()

    data class OpenMatchSummary(val match: UiMatch) : FrameUiAction()

}
