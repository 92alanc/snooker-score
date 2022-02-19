package com.alancamargo.snookerscore.domain.usecase.player

import com.alancamargo.snookerscore.domain.model.Player
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class DrawPlayerUseCaseTest {

    private val useCase = DrawPlayerUseCase()

    @Test
    fun `invoke should randomly draw a player`() {
        val player1 = Player(name = "Mark Selby")
        val player2 = Player(name = "Judd Trump")

        repeat(times = 10) {
            val result = useCase(player1, player2)
            assertThat(result).isInstanceOf(Player::class.java)
        }
    }

}
