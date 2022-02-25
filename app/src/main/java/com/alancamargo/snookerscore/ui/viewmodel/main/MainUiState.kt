package com.alancamargo.snookerscore.ui.viewmodel.main

import com.alancamargo.snookerscore.core.arch.viewmodel.UiState

data class MainUiState(val imageUrl: String? = null) : UiState {

    fun onImageUrlReceived(imageUrl: String) = copy(imageUrl = imageUrl)

}
