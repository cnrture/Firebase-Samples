package com.canerture.firebaseexamples.presentation.done

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.canerture.firebaseexamples.common.showLogDebug
import com.canerture.firebaseexamples.common.showSnack
import com.canerture.firebaseexamples.data.wrapper.AdsOperationsWrapper
import com.canerture.firebaseexamples.data.wrapper.FirestoreOperationsWrapper
import com.canerture.firebaseexamples.databinding.FragmentDoneBinding
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DoneFragment : Fragment() {

    private var _binding: FragmentDoneBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var firestoreOperations: FirestoreOperationsWrapper

    @Inject
    lateinit var adsOperationsWrapper: AdsOperationsWrapper

    private val doneAdapter by lazy { DoneAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDoneBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            rvTodos.adapter = doneAdapter

            firestoreOperations.getDoneTodosRealtime(
                onSuccess = { list ->
                    doneAdapter.submitList(list)
                },

                onFailure = {
                    view.showSnack(it)
                }
            )

            doneAdapter.onEditClick = {
                val action = DoneFragmentDirections.doneToDetail(it)
                findNavController().navigate(action)
            }

            doneAdapter.onDeleteClick = { documentId ->
                firestoreOperations.deleteTodo(documentId,
                    onSuccess = {
                        view.showSnack("Data deleted!")
                    }, onFailure = {
                        view.showSnack(it)
                    }
                )
            }

            doneAdapter.onNotDoneClick = { documentId ->
                firestoreOperations.setNotDone(documentId,
                    onSuccess = {
                        view.showSnack("Done State Updated!")
                    },
                    onFailure = {
                        requireView().showSnack(it)
                    }
                )
            }
        }

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
                    view.showSnack("Type: ${it.type} - Amount: ${it.amount}")
                }
            },

            onAdFailedToLoad = {
                showLogDebug("RewardedAds", it)
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}