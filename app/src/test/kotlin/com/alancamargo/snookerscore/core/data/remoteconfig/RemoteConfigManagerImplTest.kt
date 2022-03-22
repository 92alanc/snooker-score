package com.alancamargo.snookerscore.core.data.remoteconfig

import com.google.common.truth.Truth.assertThat
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import io.mockk.every
import io.mockk.mockk
import org.junit.Test

class RemoteConfigManagerImplTest {

    private val mockFirebaseRemoteConfig = mockk<FirebaseRemoteConfig>()
    private val remoteConfigManager = RemoteConfigManagerImpl(mockFirebaseRemoteConfig)

    @Test
    fun `getString should get string from Firebase remote config`() {
        val expected = "https://thepiratebay.org"
        every { mockFirebaseRemoteConfig.getString(any()) } returns expected

        val actual = remoteConfigManager.getString("pirate-bay")

        assertThat(actual).isEqualTo(expected)
    }

}
