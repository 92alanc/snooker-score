package com.alancamargo.snookerscore.features.player.domain.usecase

import com.alancamargo.snookerscore.features.player.domain.model.Gender
import com.alancamargo.snookerscore.features.player.domain.model.Player
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ArePlayersTheSameUseCaseTest {

    private val useCase = ArePlayersTheSameUseCase()

    @Test
    fun `when players are the same invoke should return true`() {
        val player1 = Player(id = "12345", name = "Zé Oreia", gender = Gender.MALE)
        val player2 = Player(id = "12345", name = "Zé Oreia", gender = Gender.MALE)

        val result = useCase.invoke(player1, player2)

        assertThat(result).isTrue()
    }

    @Test
    fun `when players are different invoke should return false`() {
        val player1 = Player(name = "Zé Oreia", gender = Gender.MALE)
        val player2 = Player(name = "Canela Fina", gender = Gender.MALE)

        val result = useCase.invoke(player1, player2)

        assertThat(result).isFalse()
    }

}
