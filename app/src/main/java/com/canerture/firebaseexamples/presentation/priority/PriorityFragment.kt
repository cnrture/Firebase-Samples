package com.canerture.firebaseexamples.presentation.priority

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.canerture.firebaseexamples.R
import com.canerture.firebaseexamples.common.*
import com.canerture.firebaseexamples.databinding.FragmentPriorityBinding
import com.canerture.firebaseexamples.presentation.done.DoneFragmentDirections
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

            PriorityAdapter(
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

                onDoneClick = { documentId ->
                    firestoreOperations.setDone(documentId,
                        onSuccess = {
                            requireView().showSnack("Done State Updated!")
                        },
                        onFailure = {
                            requireView().showSnack(it)
                        }
                    )
                }
            ).apply {

                listenData(this)

                radioButtonCheckedListener(rbLowPriority, rbMediumPriority, rbHighPriority) {
                    getTodoByPriority(it, this)
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
        firestoreOperations.getTodosRealtime(
            onSuccess = { list ->
                priorityAdapter.submitList(list)
                binding.rvPriorityTodos.adapter = priorityAdapter
            },
            onFailure = {
                requireView().showSnack(it)
            }
        )
    }

    private fun getTodoByPriority(priority: String, priorityAdapter: PriorityAdapter) {
        firestoreOperations.getTodoByPriorityOnce(priority,
            onSuccess = {
                priorityAdapter.submitList(it)
                binding.rvPriorityTodos.adapter = priorityAdapter
            },
            onFailure = {
                requireView().showSnack(it)
            }
        )
    }
}