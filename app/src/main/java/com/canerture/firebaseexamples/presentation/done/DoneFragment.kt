package com.canerture.firebaseexamples.presentation.done

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.canerture.firebaseexamples.R
import com.canerture.firebaseexamples.common.*
import com.canerture.firebaseexamples.databinding.FragmentDoneBinding
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DoneFragment : Fragment(R.layout.fragment_done) {

    private val binding by viewBinding(FragmentDoneBinding::bind)

    @Inject
    lateinit var firestoreOperations: FirestoreOperationsWrapper

    @Inject
    lateinit var adsOperationsWrapper: AdsOperationsWrapper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adsOperationsWrapper.loadRewardedAds(requireContext(),
            onAdLoaded = { rewardedAd ->
                rewardedAd.fullScreenContentCallback = object : FullScreenContentCallback() {
                    override fun onAdClicked() {
                        showLogDebug("RewardedAds", "onAdClicked")
                    }

                    override fun onAdDismissedFullScreenContent() {
                        showLogDebug("RewardedAds", "onAdDismissedFullScreenContent")
                    }

                    override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                        showLogDebug(
                            "RewardedAds",
                            "onAdFailedToShowFullScreenContent ${adError.message}"
                        )
                    }

                    override fun onAdImpression() {
                        showLogDebug("RewardedAds", "onAdImpression")
                    }

                    override fun onAdShowedFullScreenContent() {
                        showLogDebug("RewardedAds", "onAdShowedFullScreenContent")
                    }
                }

                rewardedAd.show(requireActivity()) {
                    requireView().showSnack("Type: ${it.type} - Amount: ${it.amount}")
                }
            },

            onAdFailedToLoad = {
                showLogDebug("RewardedAds", it)
            }
        )

        with(binding) {

            firestoreOperations.getDoneTodosRealtime(
                onSuccess = { list ->
                    DoneAdapter(
                        onEditClick = {
                            val action = DoneFragmentDirections.doneToDetail(it)
                            findNavController().navigate(action)
                        },

                        onDeleteClick = { documentId ->
                            firestoreOperations.deleteTodo(documentId,
                                onSuccess = {
                                    requireView().showSnack("Data deleted!")
                                }, onFailure = {
                                    requireView().showSnack(it)
                                }
                            )
                        },

                        onNotDoneClick = { documentId ->
                            firestoreOperations.setNotDone(documentId,
                                onSuccess = {
                                    requireView().showSnack("Done State Updated!")
                                },
                                onFailure = {
                                    requireView().showSnack(it)
                                }
                            )
                        }
                    ).apply {
                        submitList(list)
                        rvTodos.adapter = this
                    }
                },

                onFailure = {
                    requireView().showSnack(it)
                }
            )
        }
    }
}