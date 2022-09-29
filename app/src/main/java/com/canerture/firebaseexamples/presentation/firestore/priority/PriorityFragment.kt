package com.canerture.firebaseexamples.presentation.firestore.priority

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.canerture.firebaseexamples.R
import com.canerture.firebaseexamples.common.FirestoreOperationsWrapper
import com.canerture.firebaseexamples.common.radioButtonCheckedListener
import com.canerture.firebaseexamples.common.showSnack
import com.canerture.firebaseexamples.common.viewBinding
import com.canerture.firebaseexamples.databinding.FragmentPriorityBinding
import com.canerture.firebaseexamples.presentation.firestore.TodosAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PriorityFragment : Fragment(R.layout.fragment_priority) {

    private val binding by viewBinding(FragmentPriorityBinding::bind)

    @Inject
    lateinit var firestoreOperations: FirestoreOperationsWrapper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            TodosAdapter().apply {

                listenData(this)

                radioButtonCheckedListener(rbLowPriority, rbMediumPriority, rbHighPriority) {
                    getTodoByPriority(it, this)
                }

                onDoneClick = { state, documentId ->
                    firestoreOperations.updateDoneState(state, documentId, {
                        requireView().showSnack("Done State Updated!")
                    }, {
                        requireView().showSnack(it)
                    })
                }

                onEditClick = { documentId ->
                    val action =
                        PriorityFragmentDirections.priorityToDetail(
                            documentId
                        )
                    findNavController().navigate(action)
                }

                onDeleteClick = { documentId ->
                    firestoreOperations.deleteTodo(documentId, {
                        requireView().showSnack("Data deleted!")
                    }, {
                        requireView().showSnack(it)
                    })
                }
            }
        }
    }

    private fun listenData(todosAdapter: TodosAdapter) {
        firestoreOperations.getTodosRealtime({ list ->
            todosAdapter.updateList(list)
            binding.rvTodos.adapter = todosAdapter
        }, {
            requireView().showSnack(it)
        })
    }

    private fun getTodoByPriority(priority: String, todosAdapter: TodosAdapter) {
        firestoreOperations.getTodoByPriorityOnce(priority, {
            todosAdapter.updateList(it)
            binding.rvTodos.adapter = todosAdapter
        }, {
            requireView().showSnack(it)
        })
    }
}