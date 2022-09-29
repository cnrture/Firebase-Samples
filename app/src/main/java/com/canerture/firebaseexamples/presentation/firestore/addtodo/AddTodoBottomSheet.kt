package com.canerture.firebaseexamples.presentation.firestore.addtodo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.canerture.firebaseexamples.common.FirestoreOperationsWrapper
import com.canerture.firebaseexamples.common.radioButtonCheckedListener
import com.canerture.firebaseexamples.common.setCheckedTrue
import com.canerture.firebaseexamples.common.showSnack
import com.canerture.firebaseexamples.databinding.BottomSheetAddTodoBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddTodoBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetAddTodoBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var firestoreOperations: FirestoreOperationsWrapper

    private var selectedPriority = "Low"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetAddTodoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            rbLowPriority.setCheckedTrue()

            radioButtonCheckedListener(rbLowPriority, rbMediumPriority, rbHighPriority) {
                selectedPriority = it
            }

            btnAddTodo.setOnClickListener {

                val todo = etTodo.text.toString()

                if (todo.isNotEmpty()) {
                    firestoreOperations.addTodo(todo, selectedPriority, {
                        this@AddTodoBottomSheet.dismiss()
                    }, {
                        dialog?.window?.decorView?.showSnack(it)
                    })
                } else {
                    dialog?.window?.decorView?.showSnack("Todo must not be empty!")
                }
            }

            btnSetTodo.setOnClickListener {

                val todo = etTodo.text.toString()

                if (todo.isNotEmpty()) {
                    firestoreOperations.setTodo(todo, selectedPriority, {
                        this@AddTodoBottomSheet.dismiss()
                    }, {
                        dialog?.window?.decorView?.showSnack(it)
                    })
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}