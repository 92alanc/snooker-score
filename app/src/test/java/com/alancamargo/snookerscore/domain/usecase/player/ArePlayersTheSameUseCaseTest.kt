package com.alancamargo.snookerscore.domain.usecase.player

import com.alancamargo.snookerscore.domain.model.Player
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ArePlayersTheSameUseCaseTest {

    private val useCase = ArePlayersTheSameUseCase()

    @Test
    fun `when players are the same invoke should return true`() {
        val player1 = Player(name = "Zé Oreia")
        val player2 = Player(name = "Zé Oreia")

        val result = useCase.invoke(player1, player2)

        assertThat(result).isTrue()
    }

    @Test
    fun `when players are different invoke should return false`() {
        val player1 = Player(name = "Zé Oreia")
        val player2 = Player(name = "Canela Fina")

        val result = useCase.invoke(player1, player2)

        assertThat(result).isFalse()
    }

}
