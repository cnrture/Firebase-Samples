package com.canerture.firebaseexamples.presentation.firestore.statistics

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.canerture.firebaseexamples.R
import com.canerture.firebaseexamples.common.FirestoreOperationsWrapper
import com.canerture.firebaseexamples.common.viewBinding
import com.canerture.firebaseexamples.databinding.FragmentStatisticsBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class StatisticsFragment : Fragment(R.layout.fragment_statistics) {

    private val binding by viewBinding(FragmentStatisticsBinding::bind)

    @Inject
    lateinit var firestoreOperations: FirestoreOperationsWrapper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listenTodos()
    }

    private fun listenTodos() {

        with(binding) {

            firestoreOperations.getStatistics { done, notDone, lowPriority, mediumPriority, hightPriority ->
                tvDone.text = "Done: $done"
                tvNotDone.text = "Not Done: $notDone"
                tvLowPriority.text = "Low Priority: $lowPriority"
                tvMediumPriority.text = "Medium Priority: $mediumPriority"
                tvHighPriority.text = "High Priority: $hightPriority"
            }
        }
    }
}