package com.canerture.firebaseexamples.presentation.firestore

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.canerture.firebaseexamples.R
import com.canerture.firebaseexamples.common.Constants.TITLE_ALL_TODOS
import com.canerture.firebaseexamples.common.Constants.TITLE_DONE
import com.canerture.firebaseexamples.common.Constants.TITLE_PRIORITY
import com.canerture.firebaseexamples.common.Constants.TITLE_TODO
import com.canerture.firebaseexamples.common.FirestoreOperationsWrapper
import com.canerture.firebaseexamples.common.viewBinding
import com.canerture.firebaseexamples.databinding.FragmentFirestoreOperationsBinding
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FirestoreOperationsFragment : Fragment(R.layout.fragment_firestore_operations) {

    private val binding by viewBinding(FragmentFirestoreOperationsBinding::bind)

    @Inject
    lateinit var firestoreOperations: FirestoreOperationsWrapper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            val titleList = arrayListOf(TITLE_TODO, TITLE_PRIORITY, TITLE_DONE, TITLE_ALL_TODOS)

            val pagerAdapter = FirestorePagerAdapter(childFragmentManager, lifecycle)

            viewPager.adapter = pagerAdapter

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = titleList[position]
                println(tab.position)
            }.attach()
        }
    }
}