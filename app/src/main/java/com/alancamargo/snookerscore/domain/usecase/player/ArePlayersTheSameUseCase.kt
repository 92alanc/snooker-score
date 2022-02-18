package com.alancamargo.snookerscore.domain.usecase.player

import com.alancamargo.snookerscore.domain.model.Player

class ArePlayersTheSameUseCase {

    operator fun invoke(player1: Player, player2: Player): Boolean = player1 == player2

}
