package com.canerture.firebaseexamples.presentation.priority

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.canerture.firebaseexamples.R
import com.canerture.firebaseexamples.common.*
import com.canerture.firebaseexamples.databinding.FragmentPriorityBinding
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.LoadAdError
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PriorityFragment : Fragment(R.layout.fragment_priority) {

    private val binding by viewBinding(FragmentPriorityBinding::bind)

    @Inject
    lateinit var firestoreOperations: FirestoreOperationsWrapper

    @Inject
    lateinit var adsOperationsWrapper: AdsOperationsWrapper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            PriorityAdapter().apply {

                listenData(this)

                radioButtonCheckedListener(rbLowPriority, rbMediumPriority, rbHighPriority) {
                    getTodoByPriority(it, this)
                }

                onDoneClick = { state, documentId ->
                    firestoreOperations.updateDoneState(state, documentId, {
                        requireView().showSnack("Done State Updated!")
                    }, {
                        requireView().showSnack(it)
                    })
                }

                onEditClick = { documentId ->
                    val action = PriorityFragmentDirections.priorityToDetail(documentId)
                    findNavController().navigate(action)
                }

                onDeleteClick = { documentId ->
                    firestoreOperations.deleteTodo(documentId, {
                        requireView().showSnack("Data deleted!")
                    }, {
                        requireView().showSnack(it)
                    })
                }
            }

            val request = adsOperationsWrapper.showBannerAds(requireContext())
            adView.loadAd(request)
            adView.adListener = object : AdListener() {

                override fun onAdClicked() {
                    showLogDebug("BannerAds", "onAdClicked")
                }

                override fun onAdClosed() {
                    showLogDebug("BannerAds", "onAdClosed")
                }

                override fun onAdFailedToLoad(adError: LoadAdError) {
                    showLogDebug("BannerAds", "onAdFailedToLoad: ${adError.message}")
                }

                override fun onAdImpression() {
                    showLogDebug("BannerAds", "onAdImpression")
                }

                override fun onAdLoaded() {
                    showLogDebug("BannerAds", "onAdLoaded")
                }

                override fun onAdOpened() {
                    showLogDebug("BannerAds", "onAdOpened")
                }
            }
        }
    }

    private fun listenData(priorityAdapter: PriorityAdapter) {
        firestoreOperations.getTodosRealtime({ list ->
            priorityAdapter.submitList(list)
            binding.rvPriorityTodos.adapter = priorityAdapter
        }, {
            requireView().showSnack(it)
        })
    }

    private fun getTodoByPriority(priority: String, priorityAdapter: PriorityAdapter) {
        firestoreOperations.getTodoByPriorityOnce(priority, {
            priorityAdapter.submitList(it)
            binding.rvPriorityTodos.adapter = priorityAdapter
        }, {
            requireView().showSnack(it)
        })
    }
}