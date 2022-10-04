package com.canerture.firebaseexamples.presentation.firestore.statistics

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.canerture.firebaseexamples.R
import com.canerture.firebaseexamples.common.AdsOperationsWrapper
import com.canerture.firebaseexamples.common.FirestoreOperationsWrapper
import com.canerture.firebaseexamples.common.viewBinding
import com.canerture.firebaseexamples.databinding.FragmentStatisticsBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StatisticsFragment : Fragment(R.layout.fragment_statistics) {

    private val binding by viewBinding(FragmentStatisticsBinding::bind)

    @Inject
    lateinit var firestoreOperations: FirestoreOperationsWrapper

    @Inject
    lateinit var adsOperationsWrapper: AdsOperationsWrapper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adsOperationsWrapper.loadInterstitialAd(requireContext()) {
            adsOperationsWrapper.showInterstitial(requireActivity())
        }

        with(binding) {

            firestoreOperations.getStatistics { done, notDone, lowPriority, mediumPriority, highPriority ->

                val entries = arrayListOf(
                    PieEntry(done.toFloat()),
                    PieEntry(notDone.toFloat()),
                    PieEntry(lowPriority.toFloat()),
                    PieEntry(mediumPriority.toFloat()),
                    PieEntry(highPriority.toFloat())
                )

                val colors =
                    arrayListOf(Color.MAGENTA, Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW)

                val dataSet = PieDataSet(entries, "Statistics")

                dataSet.colors = colors

                val pieData = PieData(dataSet).apply {
                    setValueFormatter(PercentFormatter())
                    setValueTextSize(12f)
                    setValueTextColor(Color.BLACK)
                }

                pieChart.data = pieData

                pieChart.invalidate()
            }
        }
    }
}