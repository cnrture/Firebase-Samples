package com.canerture.firebaseexamples.data.wrapper

import com.canerture.firebaseexamples.common.showLogDebug
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlin.time.Duration.Companion.hours
import kotlin.time.DurationUnit

class RemoteConfigWrapper(private val firebaseRemoteConfig: FirebaseRemoteConfig) {

    fun initRemoteConfig() {

        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(12.hours.toLong(DurationUnit.SECONDS))
            .build()

        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)

        firebaseRemoteConfig.setDefaultsAsync(
            mapOf(
                "nativeAdsShowState" to true
            )
        )
    }

    fun fetchInterstitialAdShowState(nativeAdsShowState: (Boolean) -> Unit) {
        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener {
            nativeAdsShowState(firebaseRemoteConfig.getBoolean("nativeAdsShowState"))
        }.addOnFailureListener {
            showLogDebug(TAG, it.message.orEmpty())
        }
    }

    companion object {
        private const val TAG = "RemoteConfigWrapper"
    }
}