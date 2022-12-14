package com.canerture.firebaseexamples.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.canerture.firebaseexamples.R
import com.canerture.firebaseexamples.common.Constants.SCREEN_DONE
import com.canerture.firebaseexamples.common.Constants.SCREEN_PRIORITY
import com.canerture.firebaseexamples.common.Constants.SCREEN_STATISTICS
import com.canerture.firebaseexamples.common.Constants.SCREEN_TODOS
import com.canerture.firebaseexamples.common.Constants.TITLE_ADDITIONAL_PROVIDERS
import com.canerture.firebaseexamples.common.Constants.TITLE_NATIVE_PROVIDERS
import com.canerture.firebaseexamples.data.wrapper.AuthOperationsWrapper
import com.canerture.firebaseexamples.data.wrapper.DynamicLinksOperationsWrapper
import com.canerture.firebaseexamples.databinding.FragmentAuthBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthFragment : Fragment() {

    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var authOperations: AuthOperationsWrapper

    @Inject
    lateinit var dynamicLinksOperationsWrapper: DynamicLinksOperationsWrapper

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAuthBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authOperations.checkCurrentUser {

            dynamicLinksOperationsWrapper.subscribeDynamicLinks(requireActivity().intent,
                onSuccess = {
                    when (it) {
                        SCREEN_TODOS -> findNavController().navigate(R.id.authToTodos)
                        SCREEN_PRIORITY -> findNavController().navigate(R.id.authToPriority)
                        SCREEN_DONE -> findNavController().navigate(R.id.authToDone)
                        SCREEN_STATISTICS -> findNavController().navigate(R.id.authToStatistics)
                    }
                },
                onFailure = {
                    findNavController().navigate(R.id.authToTodos)
                })
        }

        with(binding) {

            val titleList = arrayListOf(TITLE_NATIVE_PROVIDERS, TITLE_ADDITIONAL_PROVIDERS)

            viewPager.adapter = AuthViewPagerAdapter(childFragmentManager, lifecycle)

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = titleList[position]
            }.attach()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}