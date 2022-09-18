package com.canerture.firebaseexamples.presentation.firestore

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.canerture.firebaseexamples.R
import com.canerture.firebaseexamples.common.FirestoreOperationsWrapper
import com.canerture.firebaseexamples.common.showSnack
import com.canerture.firebaseexamples.common.viewBinding
import com.canerture.firebaseexamples.databinding.FragmentGetDataOnceBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class GetDataOnceFragment : Fragment(R.layout.fragment_get_data_once) {

    private val binding by viewBinding(FragmentGetDataOnceBinding::bind)

    private val contactsAdapter by lazy { ContactsAdapter() }

    @Inject
    lateinit var firestoreOperations: FirestoreOperationsWrapper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            firestoreOperations.getDataOnce({ list ->

                contactsAdapter.apply {

                    updateList(list)
                    rvContacts.adapter = this

                    onDetailClick = { documentId ->
                        val action =
                            FirestoreOperationsFragmentDirections.firestoreOperationsToDetail(
                                documentId
                            )
                        findNavController().navigate(action)
                    }

                    onDeleteClick = { documentId ->
                        firestoreOperations.deleteData(documentId, {
                            requireView().showSnack("Data deleted!")
                        }, {
                            requireView().showSnack(it)
                        })
                    }
                }
            }, {
                requireView().showSnack(it)
            })
        }
    }
}