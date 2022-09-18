package com.canerture.firebaseexamples.presentation.firestore

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class FirestorePagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return GetDataOnceFragment()
            1 -> return RealtimeUpdatesFragment()
            2 -> return QueryDataFragment()
        }
        return GetDataOnceFragment()
    }
}