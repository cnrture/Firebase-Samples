package com.canerture.firebaseexamples.presentation.firestore

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.canerture.firebaseexamples.R
import com.canerture.firebaseexamples.common.FirestoreOperationsWrapper
import com.canerture.firebaseexamples.common.viewBinding
import com.canerture.firebaseexamples.databinding.FragmentRealtimeUpdatesBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RealtimeUpdatesFragment : Fragment(R.layout.fragment_realtime_updates) {

    private val binding by viewBinding(FragmentRealtimeUpdatesBinding::bind)

    private val contactsAdapter by lazy { ContactsAdapter() }

    @Inject
    lateinit var firestoreOperations: FirestoreOperationsWrapper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            firestoreOperations.getDataWithRealtimeUpdates({
                contactsAdapter.updateList(it)
                rvContacts.adapter = contactsAdapter
            }, {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            })

            contactsAdapter.onItemClick = {
                val action = FirestoreOperationsFragmentDirections.firestoreOperationsToDetail(it)
                findNavController().navigate(action)
            }
        }
    }
}