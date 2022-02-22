package com.alancamargo.snookerscore.data.remote

import com.alancamargo.snookerscore.core.remoteconfig.RemoteConfigManager
import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.junit.Test

class RulesUrlRemoteDataSourceImplTest {

    private val mockRemoteConfigManager = mockk<RemoteConfigManager>()
    private val remoteDataSource = RulesUrlRemoteDataSourceImpl(mockRemoteConfigManager)

    @Test
    fun `getRulesUrl should return url from remote config`() {
        val expected = "https://megaupload.com"
        every { mockRemoteConfigManager.getString(key = "rules_url") } returns expected

        val actual = remoteDataSource.getRulesUrl()

        assertThat(actual).isEqualTo(expected)
    }

}
