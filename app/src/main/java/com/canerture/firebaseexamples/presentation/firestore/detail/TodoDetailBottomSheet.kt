package com.canerture.firebaseexamples.presentation.firestore.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.canerture.firebaseexamples.common.Constants.PRIORITY_HIGH
import com.canerture.firebaseexamples.common.Constants.PRIORITY_LOW
import com.canerture.firebaseexamples.common.Constants.PRIORITY_MEDIUM
import com.canerture.firebaseexamples.common.FirestoreOperationsWrapper
import com.canerture.firebaseexamples.common.radioButtonCheckedListener
import com.canerture.firebaseexamples.common.setCheckedTrue
import com.canerture.firebaseexamples.common.showSnack
import com.canerture.firebaseexamples.databinding.BottomSheetTodoDetailBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TodoDetailBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetTodoDetailBinding? = null
    private val binding get() = _binding!!

    private val args: TodoDetailBottomSheetArgs by navArgs()

    @Inject
    lateinit var firestoreOperations: FirestoreOperationsWrapper

    private var selectedPriority = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetTodoDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            radioButtonCheckedListener(rbLowPriority, rbMediumPriority, rbHighPriority) {
                selectedPriority = it
            }

            firestoreOperations.getTodoByDocumentIdOnce(args.documentId, {
                etTodo.setText(it.todo)
                selectedPriority = it.priority ?: PRIORITY_LOW
                when (it.priority) {
                    PRIORITY_LOW -> rbLowPriority.setCheckedTrue()
                    PRIORITY_MEDIUM -> rbMediumPriority.setCheckedTrue()
                    PRIORITY_HIGH -> rbHighPriority.setCheckedTrue()
                }
            }, {
                dialog?.window?.decorView?.showSnack(it)
            })

            btnUpdateData.setOnClickListener {

                val todo = etTodo.text.toString()

                if (todo.isNotEmpty()) {
                    firestoreOperations.updateTodo(todo, selectedPriority, args.documentId, {
                        this@TodoDetailBottomSheet.dismiss()
                    }, {
                        dialog?.window?.decorView?.showSnack(it)
                    })
                } else {
                    dialog?.window?.decorView?.showSnack("Todo must not be empty!")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}