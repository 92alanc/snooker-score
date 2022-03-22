package com.alancamargo.snookerscore.features.main.domain.usecase

import com.alancamargo.snookerscore.features.main.domain.repository.RulesUrlRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.junit.Test

class GetRulesUrlUseCaseTest {

    private val mockRepository = mockk<RulesUrlRepository>()
    private val useCase = GetRulesUrlUseCase(mockRepository)

    @Test
    fun `invoke should return url from repository`() {
        val expected = "https://thepiratebay.org"
        every { mockRepository.getRulesUrl() } returns expected

        val actual = useCase()

        assertThat(actual).isEqualTo(expected)
    }

}
