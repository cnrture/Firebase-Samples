package com.canerture.firebaseexamples.presentation.firestore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import com.canerture.firebaseexamples.common.FirestoreOperationsWrapper
import com.canerture.firebaseexamples.data.model.Contact
import com.canerture.firebaseexamples.databinding.ContactDetailBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ContactDetailBottomSheet : BottomSheetDialogFragment() {

    private var _binding: ContactDetailBottomSheetBinding? = null
    private val binding get() = _binding!!

    private val args: ContactDetailBottomSheetArgs by navArgs()

    @Inject
    lateinit var firestoreOperations: FirestoreOperationsWrapper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ContactDetailBottomSheetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            firestoreOperations.searchDocument(args.documentId, {
                etName.setText(it.name)
                etSurname.setText(it.surname)
                etEmail.setText(it.email)
            }, {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            })

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
        }
    }

    private fun collectAndCheckData(): Contact? {

        with(binding) {

            val name = etName.text.toString()
            val surname = etSurname.text.toString()
            val email = etEmail.text.toString()

            return if (name.isNotEmpty() && surname.isNotEmpty() && email.isNotEmpty()) {
                Contact(args.documentId, name, surname, email)
            } else {
                null
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}