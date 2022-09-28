package com.canerture.firebaseexamples.presentation.firestore.todos

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.canerture.firebaseexamples.R
import com.canerture.firebaseexamples.common.FirestoreOperationsWrapper
import com.canerture.firebaseexamples.common.showSnack
import com.canerture.firebaseexamples.common.viewBinding
import com.canerture.firebaseexamples.databinding.FragmentTodosBinding
import com.canerture.firebaseexamples.presentation.firestore.FirestoreOperationsFragmentDirections
import com.canerture.firebaseexamples.presentation.firestore.TodosAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TodosFragment : Fragment(R.layout.fragment_todos) {

    private val binding by viewBinding(FragmentTodosBinding::bind)

    private val todosAdapter by lazy { TodosAdapter() }

    @Inject
    lateinit var firestoreOperations: FirestoreOperationsWrapper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listenTodos()
        initUI()
    }

    private fun initUI() {

        with(binding) {

            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(query: String?): Boolean {
                    if (query.isNullOrEmpty()) {
                        listenTodos()
                    }   else {
                        queryTodo(query)
                    }
                    return false
                }
            })

            fabAddTodo.setOnClickListener {
                findNavController().navigate(R.id.firestoreOperationsToAddTodo)
            }
        }
    }

    private fun listenTodos() {

        firestoreOperations.getNotDoneTodosWithRealtimeUpdates({ list ->

            todosAdapter.apply {

                updateList(list)
                binding.rvTodos.adapter = this

                onDoneClick = { state, documentId ->
                    firestoreOperations.updateDoneState(state, documentId, {
                        requireView().showSnack("Done State Updated!")
                    }, {
                        requireView().showSnack(it)
                    })
                }

                onEditClick = { documentId ->
                    val action =
                        FirestoreOperationsFragmentDirections.firestoreOperationsToDetail(
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
        }, {
            requireView().showSnack(it)
        })
    }

    private fun queryTodo(query: String) {
        firestoreOperations.queryTodo(query, { list ->
            todosAdapter.updateList(list)
            binding.rvTodos.adapter = todosAdapter
        }, { errorMessage ->
            requireView().showSnack(errorMessage)
        })
    }
}