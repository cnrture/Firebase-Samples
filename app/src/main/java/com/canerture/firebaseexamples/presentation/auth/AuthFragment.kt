package com.canerture.firebaseexamples.presentation.auth

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.canerture.firebaseexamples.R
import com.canerture.firebaseexamples.common.viewBinding
import com.canerture.firebaseexamples.databinding.FragmentAuthBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthFragment : Fragment(R.layout.fragment_auth) {

    private val binding by viewBinding(FragmentAuthBinding::bind)

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        firebaseAuth.currentUser?.let {

        }

        with(binding) {

            val titleList = arrayListOf(NATIVE_PROVIDERS, ADDITIONAL_PROVIDERS)

            viewPager.adapter = AuthViewPagerAdapter(childFragmentManager, lifecycle)

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = titleList[position]
            }.attach()

            btnSignOut.setOnClickListener {
                firebaseAuth.signOut()
            }
        }
    }

    companion object {
        private const val NATIVE_PROVIDERS = "Native Providers"
        private const val ADDITIONAL_PROVIDERS = "Additional Providers"
    }
}