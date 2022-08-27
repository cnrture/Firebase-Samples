package com.canerture.firebaseexamples.presentation.firestore

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.canerture.firebaseexamples.R
import com.canerture.firebaseexamples.common.FirestoreOperationsWrapper
import com.canerture.firebaseexamples.common.viewBinding
import com.canerture.firebaseexamples.data.model.Contact
import com.canerture.firebaseexamples.databinding.FragmentFirestoreOperationsBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class FirestoreOperationsFragment : Fragment(R.layout.fragment_firestore_operations) {

    private val binding by viewBinding(FragmentFirestoreOperationsBinding::bind)

    private val contactsAdapter by lazy { ContactsAdapter() }

    @Inject
    lateinit var firestoreOperations: FirestoreOperationsWrapper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            btnAddData.setOnClickListener {

                collectAndCheckData()?.let { contact ->

                    firestoreOperations.addData(contact, {
                        Toast.makeText(requireContext(), "Data added!", Toast.LENGTH_SHORT).show()
                    }, {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    })
                } ?: run {
                    Toast.makeText(requireContext(), "Missing data!", Toast.LENGTH_SHORT).show()
                }
            }

            btnSetData.setOnClickListener {

                collectAndCheckData()?.let { contact ->

                    firestoreOperations.setData(contact, {
                        Toast.makeText(requireContext(), "Data set!", Toast.LENGTH_SHORT).show()
                    }, {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    })
                } ?: run {
                    Toast.makeText(requireContext(), "Missing data!", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            btnUpdateData.setOnClickListener {

                collectAndCheckData()?.let { contact ->

                    firestoreOperations.updateData(contact, {
                        Toast.makeText(requireContext(), "Data updated!", Toast.LENGTH_SHORT).show()
                    }, {
                        Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                    })
                } ?: run {
                    Toast.makeText(requireContext(), "Missing data!", Toast.LENGTH_SHORT).show()
                }
            }

            firestoreOperations.getDataWithRealtimeUpdates({
                contactsAdapter.updateList(it)
                rvData.adapter = contactsAdapter
            }, {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            })
        }
    }

    private fun collectAndCheckData(): Contact? {

        with(binding) {

            val name = etName.text.toString()
            val surname = etSurname.text.toString()
            val email = etEmail.text.toString()

            return if (name.isNotEmpty() && surname.isNotEmpty() && email.isNotEmpty()) {
                Contact(name, surname, email)
            } else {
                null
            }
        }
    }
}