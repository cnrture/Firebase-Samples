package com.canerture.firebaseexamples.presentation.auth

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.canerture.firebaseexamples.presentation.auth.additionalproviders.AdditionalProvidersFragment
import com.canerture.firebaseexamples.presentation.auth.nativeproviders.NativeProvidersFragment

class AuthViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return NativeProvidersFragment()
            1 -> return AdditionalProvidersFragment()
        }
        return NativeProvidersFragment()
    }
}