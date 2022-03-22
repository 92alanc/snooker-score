package com.alancamargo.snookerscore.features.frame.domain.usecase

import com.alancamargo.snookerscore.features.frame.domain.model.Foul
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class GetPenaltyValueUseCaseTest {

    private val useCase = GetPenaltyValueUseCase()

    @Test
    fun `invoke should return fixed penalty value`() {
        val foul = Foul.Other

        val penaltyValue = useCase.invoke(foul)

        assertThat(penaltyValue).isEqualTo(7)
    }

}
