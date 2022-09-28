package com.canerture.firebaseexamples.presentation.firestore.addtodo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.canerture.firebaseexamples.common.FirestoreOperationsWrapper
import com.canerture.firebaseexamples.common.showSnack
import com.canerture.firebaseexamples.data.model.Todo
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

    private var selectedPriority = ""

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

            rbLowPriority.checked()
            rbMediumPriority.checked()
            rbHighPriority.checked()

            btnAddTodo.setOnClickListener {

                val todo = etTodo.text.toString()

                if (todo.isNotEmpty()) {

                    val todoModel = Todo(todo = todo, priority = selectedPriority)

                    firestoreOperations.addTodo(todoModel, {
                        requireView().showSnack("Data added!")
                        this@AddTodoBottomSheet.dismiss()
                    }, {
                        requireView().showSnack(it)
                    })
                }
            }

            btnSetTodo.setOnClickListener {

                val todo = etTodo.text.toString()

                if (todo.isNotEmpty()) {

                    val todoModel = Todo(todo = todo, priority = selectedPriority)

                    firestoreOperations.setTodo(todoModel, {
                        requireView().showSnack("Data set!")
                        this@AddTodoBottomSheet.dismiss()
                    }, {
                        requireView().showSnack(it)
                    })
                }
            }
        }
    }

    private fun RadioButton.checked() {

        setOnClickListener {

            with (binding) {
                when (it.id) {
                    rbLowPriority.id -> {
                        rbMediumPriority.isChecked = false
                        rbHighPriority.isChecked = false
                        selectedPriority = rbLowPriority.text.toString()
                    }
                    rbMediumPriority.id -> {
                        rbLowPriority.isChecked = false
                        rbHighPriority.isChecked = false
                        selectedPriority = rbMediumPriority.text.toString()
                    }
                    rbHighPriority.id -> {
                        rbLowPriority.isChecked = false
                        rbMediumPriority.isChecked = false
                        selectedPriority = rbHighPriority.text.toString()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}