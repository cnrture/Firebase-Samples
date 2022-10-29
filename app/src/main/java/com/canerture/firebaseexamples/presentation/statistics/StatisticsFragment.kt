package com.canerture.firebaseexamples.presentation.statistics

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.canerture.firebaseexamples.R
import com.canerture.firebaseexamples.common.showLogDebug
import com.canerture.firebaseexamples.common.showSnack
import com.canerture.firebaseexamples.data.wrapper.AdsOperationsWrapper
import com.canerture.firebaseexamples.data.wrapper.FirestoreOperationsWrapper
import com.canerture.firebaseexamples.data.wrapper.RemoteConfigWrapper
import com.canerture.firebaseexamples.databinding.FragmentStatisticsBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StatisticsFragment : Fragment() {

    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var firestoreOperations: FirestoreOperationsWrapper

    @Inject
    lateinit var adsOperationsWrapper: AdsOperationsWrapper

    @Inject
    lateinit var remoteConfigWrapper: RemoteConfigWrapper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatisticsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        remoteConfigWrapper.fetchInterstitialAdShowState {
            if (it) {
                adsOperationsWrapper.loadInterstitialAd(
                    onAdLoaded = { interstitialAd ->
                        interstitialAd.fullScreenContentCallback =
                            object : FullScreenContentCallback() {
                                override fun onAdDismissedFullScreenContent() {
                                    showLogDebug(
                                        "InterstitialAds",
                                        "onAdDismissedFullScreenContent"
                                    )
                                }

                                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                                    showLogDebug(
                                        "InterstitialAds",
                                        "onAdFailedToShowFullScreenContent: ${adError.message}"
                                    )
                                }

                                override fun onAdShowedFullScreenContent() {
                                    showLogDebug("InterstitialAds", "onAdShowedFullScreenContent")
                                }
                            }
                        interstitialAd.show(requireActivity())
                    },

                    onAdFailedToLoad = {
                        showLogDebug("InterstitialAds", "onAdShowedFullScreenContent")
                    })
            }
        }

        with(binding) {

            firestoreOperations.getStatistics(
                onSuccess = { done, notDone, lowPriority, mediumPriority, highPriority ->

                    val entries = arrayListOf(
                        PieEntry(done.toFloat(), "Done"),
                        PieEntry(notDone.toFloat(), "Not Done"),
                        PieEntry(lowPriority.toFloat(), "Low"),
                        PieEntry(mediumPriority.toFloat(), "Medium"),
                        PieEntry(highPriority.toFloat(), "High")
                    )

                    val colors = arrayListOf(
                        ContextCompat.getColor(requireContext(), R.color.stats_purple),
                        ContextCompat.getColor(requireContext(), R.color.stats_blue),
                        ContextCompat.getColor(requireContext(), R.color.stats_red),
                        ContextCompat.getColor(requireContext(), R.color.stats_green),
                        ContextCompat.getColor(requireContext(), R.color.stats_yellow)
                    )

                    val dataSet = PieDataSet(entries, "Statistics")
                    dataSet.colors = colors
                    dataSet.formSize = 15f

                    val pieData = PieData(dataSet).apply {
                        setValueFormatter(PercentFormatter())
                        setValueTextSize(15f)
                        setValueTextColor(Color.WHITE)
                    }
                    pieChart.data = pieData
                    pieChart.invalidate()
                },

                onFailure = {
                    view.showSnack(it)
                })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}