package com.alancamargo.snookerscore.features.player.domain.usecase

import com.alancamargo.snookerscore.features.player.domain.model.Player

class ArePlayersTheSameUseCase {

    operator fun invoke(player1: Player, player2: Player): Boolean = player1 == player2

}
