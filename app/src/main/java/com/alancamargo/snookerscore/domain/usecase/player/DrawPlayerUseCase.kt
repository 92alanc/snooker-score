package com.alancamargo.snookerscore.domain.usecase.player

import com.alancamargo.snookerscore.domain.model.Player
import java.util.Random

class DrawPlayerUseCase {

    operator fun invoke(player1: Player, player2: Player): Player {
        return when (Random().nextInt(2)) {
            0 -> player1
            1 -> player2
            else -> throw IllegalStateException("Result must be either 0 or 1")
        }
    }

}
