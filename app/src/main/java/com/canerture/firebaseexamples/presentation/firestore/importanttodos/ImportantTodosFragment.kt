package com.canerture.firebaseexamples.presentation.firestore.importanttodos

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.canerture.firebaseexamples.R
import com.canerture.firebaseexamples.common.FirestoreOperationsWrapper
import com.canerture.firebaseexamples.common.showSnack
import com.canerture.firebaseexamples.common.viewBinding
import com.canerture.firebaseexamples.databinding.FragmentImportantTodosBinding
import com.canerture.firebaseexamples.presentation.firestore.FirestoreOperationsFragmentDirections
import com.canerture.firebaseexamples.presentation.firestore.TodosAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ImportantTodosFragment : Fragment(R.layout.fragment_important_todos) {

    private val binding by viewBinding(FragmentImportantTodosBinding::bind)

    private val todosAdapter by lazy { TodosAdapter() }

    @Inject
    lateinit var firestoreOperations: FirestoreOperationsWrapper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listenData()

        with(binding) {

            rbAll.checked()
            rbLowPriority.checked()
            rbMediumPriority.checked()
            rbHighPriority.checked()

            todosAdapter.onDoneClick = { state, documentId ->
                firestoreOperations.updateDoneState(state, documentId, {
                    requireView().showSnack("Done State Updated!")
                }, {
                    requireView().showSnack(it)
                })
            }

            todosAdapter.onEditClick = { documentId ->
                val action =
                    FirestoreOperationsFragmentDirections.firestoreOperationsToDetail(
                        documentId
                    )
                findNavController().navigate(action)
            }

            todosAdapter.onDeleteClick = { documentId ->
                firestoreOperations.deleteTodo(documentId, {
                    requireView().showSnack("Data deleted!")
                }, {
                    requireView().showSnack(it)
                })
            }
        }
    }

    private fun RadioButton.checked() {

        setOnClickListener {

            with (binding) {
                when (it.id) {
                    rbAll.id -> {
                        rbLowPriority.isChecked = false
                        rbMediumPriority.isChecked = false
                        rbHighPriority.isChecked = false
                        listenData()
                    }
                    rbLowPriority.id -> {
                        rbAll.isChecked = false
                        rbMediumPriority.isChecked = false
                        rbHighPriority.isChecked = false
                        queryTodoByPriority(rbLowPriority.text.toString())
                    }
                    rbMediumPriority.id -> {
                        rbAll.isChecked = false
                        rbLowPriority.isChecked = false
                        rbHighPriority.isChecked = false
                        queryTodoByPriority(rbMediumPriority.text.toString())
                    }
                    rbHighPriority.id -> {
                        rbAll.isChecked = false
                        rbLowPriority.isChecked = false
                        rbMediumPriority.isChecked = false
                        queryTodoByPriority(rbHighPriority.text.toString())
                    }
                }
            }
        }
    }

    private fun listenData() {
        firestoreOperations.getTodosOnce({ list ->
            todosAdapter.updateList(list)
            binding.rvTodos.adapter = todosAdapter
        }, {
            requireView().showSnack(it)
        })
    }

    private fun queryTodoByPriority(priority: String) {
        firestoreOperations.queryTodoByPriority(priority, {
            todosAdapter.updateList(it)
            binding.rvTodos.adapter = todosAdapter
        }, {
            requireView().showSnack(it)
        })
    }
}