package com.canerture.firebaseexamples.common

import android.content.Context
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class AdsOperationsWrapper {

    fun initMobileAds(context: Context) {
        MobileAds.initialize(context)
    }

    fun showBannerAds(context: Context): AdRequest {
        AdView(context).apply {
            setAdSize(AdSize.BANNER)
            adUnitId = "ca-app-pub-3940256099942544/6300978111"
        }
        return AdRequest.Builder().build()
    }

    fun loadInterstitialAd(
        context: Context,
        onAdLoaded: (InterstitialAd) -> Unit,
        onAdFailedToLoad: (String) -> Unit
    ) {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            context,
            "ca-app-pub-3940256099942544/1033173712",
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
        context: Context,
        onAdLoaded: (RewardedAd) -> Unit,
        onAdFailedToLoad: (String) -> Unit
    ) {

        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(
            context,
            "ca-app-pub-3940256099942544/5224354917",
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
        context: Context,
        onLoadedAd: (NativeAd) -> Unit,
        onAdFailedToLoad: (String) -> Unit
    ) {
        val adLoader = AdLoader.Builder(context, "ca-app-pub-3940256099942544/2247696110")
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
}