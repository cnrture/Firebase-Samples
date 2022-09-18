package com.canerture.firebaseexamples.presentation.firestore

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.canerture.firebaseexamples.R
import com.canerture.firebaseexamples.common.FirestoreOperationsWrapper
import com.canerture.firebaseexamples.common.showSnack
import com.canerture.firebaseexamples.common.viewBinding
import com.canerture.firebaseexamples.data.model.Contact
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

            val titleList = arrayListOf("Get Data Once", "Realtime Updates", "Query Data")

            viewPager.adapter = FirestorePagerAdapter(childFragmentManager, lifecycle)

            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = titleList[position]
            }.attach()

            btnAddData.setOnClickListener {

                collectAndCheckData()?.let { contact ->

                    firestoreOperations.addData(contact, {
                        requireView().showSnack("Data added!")
                    }, {
                        requireView().showSnack(it)
                    })
                } ?: run {
                    requireView().showSnack("Missing data!")
                }
            }

            btnSetData.setOnClickListener {

                collectAndCheckData()?.let { contact ->

                    firestoreOperations.setData(contact, {
                        requireView().showSnack("Data set!")
                    }, {
                        requireView().showSnack(it)
                    })
                } ?: run {
                    requireView().showSnack("Missing data!")
                }
            }
        }
    }

    private fun collectAndCheckData(): Contact? {

        with(binding) {

            val name = etName.text.toString()
            val surname = etSurname.text.toString()
            val email = etEmail.text.toString()

            return if (name.isNotEmpty() && surname.isNotEmpty() && email.isNotEmpty()) {
                Contact(name = name, surname = surname, email = email)
            } else {
                null
            }
        }
    }
}