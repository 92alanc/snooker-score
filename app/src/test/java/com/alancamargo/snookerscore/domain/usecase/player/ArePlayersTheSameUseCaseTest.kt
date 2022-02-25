package com.alancamargo.snookerscore.domain.usecase.player

import com.alancamargo.snookerscore.domain.model.Gender
import com.alancamargo.snookerscore.domain.model.Player
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class ArePlayersTheSameUseCaseTest {

    private val useCase = ArePlayersTheSameUseCase()

    @Test
    fun `when players are the same invoke should return true`() {
        val player1 = Player(name = "Zé Oreia", gender = Gender.MALE)
        val player2 = Player(name = "Zé Oreia", gender = Gender.MALE)

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
