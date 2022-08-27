package com.canerture.firebaseexamples.presentation.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.canerture.firebaseexamples.R
import com.canerture.firebaseexamples.common.AuthOperationsWrapper
import com.canerture.firebaseexamples.common.viewBinding
import com.canerture.firebaseexamples.databinding.FragmentAuthBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthFragment : Fragment(R.layout.fragment_auth) {

    private val binding by viewBinding(FragmentAuthBinding::bind)

    @Inject
    lateinit var authOperations: AuthOperationsWrapper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        authOperations.checkCurrentUser {
            findNavController().navigate(R.id.authToFirestoreOperations)
        }

        with(binding) {

            val titleList = arrayListOf("Native Providers", "Additional Providers")

            viewPager.adapter = AuthViewPagerAdapter(childFragmentManager, lifecycle)

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = titleList[position]
            }.attach()

            btnSignOut.setOnClickListener {
                authOperations.signOut()
            }
        }
    }
}