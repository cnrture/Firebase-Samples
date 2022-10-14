package com.canerture.firebaseexamples.data.wrapper

import android.content.Context
import com.canerture.firebaseexamples.common.showLogDebug
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class AdsOperationsWrapper(private val context: Context) {

    fun initMobileAds() {
        MobileAds.initialize(context)
    }

    fun showBannerAds(): AdRequest = AdRequest.Builder().build()

    fun loadInterstitialAd(
        onAdLoaded: (InterstitialAd) -> Unit,
        onAdFailedToLoad: (String) -> Unit
    ) {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            context,
            INTERSTITIAL_ADS_ID,
            adRequest,
            object : InterstitialAdLoadCallback() {

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    onAdLoaded(interstitialAd)
                    showLogDebug("InterstitialAds", "onAdLoaded")
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    onAdFailedToLoad(adError.message)
                }
            }
        )
    }

    fun loadRewardedAds(
        onAdLoaded: (RewardedAd) -> Unit,
        onAdFailedToLoad: (String) -> Unit
    ) {

        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(
            context,
            REWARDED_ADS_ID,
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    onAdFailedToLoad(adError.message)
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    onAdLoaded(rewardedAd)
                }
            })
    }

    fun loadNativeAds(
        onLoadedAd: (NativeAd) -> Unit,
        onAdFailedToLoad: (String) -> Unit
    ) {
        val adLoader = AdLoader.Builder(context, NATIVE_ADS_ID)
            .forNativeAd { ad: NativeAd ->
                onLoadedAd(ad)
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    onAdFailedToLoad(adError.message)
                }
            }).build()
        adLoader.loadAd(AdRequest.Builder().build())
    }

    companion object {
        private const val NATIVE_ADS_ID = "ca-app-pub-3940256099942544/2247696110"
        private const val REWARDED_ADS_ID = "ca-app-pub-3940256099942544/5224354917"
        private const val INTERSTITIAL_ADS_ID = "ca-app-pub-3940256099942544/1033173712"
    }
}