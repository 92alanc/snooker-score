package com.alancamargo.snookerscore.data.repository

import com.alancamargo.snookerscore.data.remote.RulesUrlRemoteDataSource
import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import org.junit.Test

class RulesUrlRepositoryImplTest {

    private val mockRemoteDataSource = mockk<RulesUrlRemoteDataSource>()
    private val repository = RulesUrlRepositoryImpl(mockRemoteDataSource)

    @Test
    fun `getRulesUrl should return url from remote data source`() {
        val expected = "https://thepiratebay.org"
        every { mockRemoteDataSource.getRulesUrl() } returns expected

        val actual = repository.getRulesUrl()

        Truth.assertThat(actual).isEqualTo(expected)
    }

}