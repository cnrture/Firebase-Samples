package com.canerture.firebaseexamples.presentation.priority

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.canerture.firebaseexamples.common.radioButtonCheckedListener
import com.canerture.firebaseexamples.common.showLogDebug
import com.canerture.firebaseexamples.common.showSnack
import com.canerture.firebaseexamples.data.wrapper.AdsOperationsWrapper
import com.canerture.firebaseexamples.data.wrapper.FirestoreOperationsWrapper
import com.canerture.firebaseexamples.databinding.FragmentPriorityBinding
import com.canerture.firebaseexamples.presentation.done.DoneFragmentDirections
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.LoadAdError
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PriorityFragment : Fragment() {

    private var _binding: FragmentPriorityBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var firestoreOperations: FirestoreOperationsWrapper

    @Inject
    lateinit var adsOperationsWrapper: AdsOperationsWrapper

    private val priorityAdapter by lazy { PriorityAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPriorityBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            rvPriorityTodos.adapter = priorityAdapter

            listenData(priorityAdapter)

            radioButtonCheckedListener(rbLowPriority, rbMediumPriority, rbHighPriority) {
                getTodoByPriority(it, priorityAdapter)
            }

            priorityAdapter.onEditClick = {
                val action = DoneFragmentDirections.doneToDetail(it)
                findNavController().navigate(action)
            }

            priorityAdapter.onDeleteClick = { documentId ->
                firestoreOperations.deleteTodo(documentId,
                    onSuccess = {
                        view.showSnack("Data deleted!")
                    }, onFailure = {
                        view.showSnack(it)
                    }
                )
            }

            priorityAdapter.onDoneOrNotDoneClick = { isChecked, documentId ->
                firestoreOperations.setDoneState(isChecked, documentId,
                    onSuccess = {
                        view.showSnack("Done State Updated!")
                    },
                    onFailure = {
                        view.showSnack(it)
                    }
                )
            }

            val request = adsOperationsWrapper.showBannerAds()
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
            },
            onFailure = {
                requireView().showSnack(it)
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}