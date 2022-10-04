package com.canerture.firebaseexamples.common

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback

class AdsOperationsWrapper {

    var interstitialAd: InterstitialAd? = null
    var rewardedAd: RewardedAd? = null

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

    fun loadInterstitialAd(context: Context, onAdLoaded: () -> Unit = {}) {
        val adRequest = AdRequest.Builder().build()

        InterstitialAd.load(
            context,
            "ca-app-pub-3940256099942544/1033173712",
            adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    interstitialAd = null
                    showLogDebug("InterstitialAds", "onAdFailedToLoad")
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    this@AdsOperationsWrapper.interstitialAd = interstitialAd
                    onAdLoaded()
                    showLogDebug("InterstitialAds", "onAdLoaded")
                }
            }
        )
    }

    fun showInterstitial(activity: Activity) {
        interstitialAd?.fullScreenContentCallback =
            object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    interstitialAd = null
                    loadInterstitialAd(activity)
                    showLogDebug("InterstitialAds", "onAdDismissedFullScreenContent")
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    interstitialAd = null
                    showLogDebug(
                        "InterstitialAds",
                        "onAdFailedToShowFullScreenContent: ${adError.message}"
                    )
                }

                override fun onAdShowedFullScreenContent() {
                    showLogDebug("InterstitialAds", "onAdShowedFullScreenContent")
                }
            }
        interstitialAd?.show(activity)
    }

    fun loadRewardedAds(context: Context, onAdLoaded: () -> Unit = {}) {

        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(
            context,
            "ca-app-pub-3940256099942544/5224354917",
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    rewardedAd = null
                    showLogDebug("RewardedAds", "onAdFailedToLoad")
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    this@AdsOperationsWrapper.rewardedAd = rewardedAd
                    onAdLoaded()
                    showLogDebug("RewardedAds", "onAdLoaded")
                }
            })
    }

    fun showRewardedAds(activity: Activity) {

        rewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdClicked() {
                showLogDebug("RewardedAds", "onAdClicked")
            }

            override fun onAdDismissedFullScreenContent() {
                showLogDebug("RewardedAds", "onAdDismissedFullScreenContent")
                rewardedAd = null
                loadRewardedAds(activity)
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                showLogDebug("RewardedAds", "onAdFailedToShowFullScreenContent ${adError.message}")
                rewardedAd = null
                loadRewardedAds(activity)
            }

            override fun onAdImpression() {
                showLogDebug("RewardedAds", "onAdImpression")
            }

            override fun onAdShowedFullScreenContent() {
                showLogDebug("RewardedAds", "onAdShowedFullScreenContent")
            }
        }

        rewardedAd?.show(activity) {
            showLogDebug("RewardedAds", "${it.amount}, ${it.type}")
        }
    }

    fun loadNativeAds(
        context: Context,
        loadedAd: (NativeAd) -> Unit,
        failedLoad: () -> Unit
    ) {
        val adLoader = AdLoader.Builder(context, "ca-app-pub-3940256099942544/2247696110")
            .forNativeAd { ad: NativeAd ->
                loadedAd(ad)
                showLogDebug("NativeAds", "forNativeAd")
            }
            .withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    failedLoad()
                    showLogDebug("NativeAds", "onAdFailedToLoad: ${adError.message}")
                }
            }).build()
        adLoader.loadAd(AdRequest.Builder().build())
    }
}