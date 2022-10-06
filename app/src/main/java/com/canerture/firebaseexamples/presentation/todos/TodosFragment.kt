package com.canerture.firebaseexamples.presentation.todos

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.canerture.firebaseexamples.R
import com.canerture.firebaseexamples.common.AdsOperationsWrapper
import com.canerture.firebaseexamples.common.FirestoreOperationsWrapper
import com.canerture.firebaseexamples.common.showSnack
import com.canerture.firebaseexamples.common.viewBinding
import com.canerture.firebaseexamples.data.model.Todo
import com.canerture.firebaseexamples.databinding.FragmentTodosBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TodosFragment : Fragment(R.layout.fragment_todos) {

    private val binding by viewBinding(FragmentTodosBinding::bind)

    @Inject
    lateinit var firestoreOperations: FirestoreOperationsWrapper

    @Inject
    lateinit var adsOperationsWrapper: AdsOperationsWrapper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            TodosAdapter().apply {

                firestoreOperations.getNotDoneTodosRealtime({ list ->

                    adsOperationsWrapper.loadNativeAds(requireContext(), {
                        setNativeAds(it)
                        submitList(addNullToArray(list))
                        binding.rvTodos.adapter = this
                    }, {
                        submitList(list)
                    })

                    onDoneClick = { state, documentId ->
                        firestoreOperations.updateDoneState(state, documentId, {
                            requireView().showSnack("Done State Updated!")
                        }, {
                            requireView().showSnack(it)
                        })
                    }

                    onEditClick = { documentId ->
                        val action = TodosFragmentDirections.todosToDetail(documentId)
                        findNavController().navigate(action)
                    }

                    onDeleteClick = { documentId ->
                        firestoreOperations.deleteTodo(documentId, {
                            requireView().showSnack("Data deleted!")
                        }, {
                            requireView().showSnack(it)
                        })
                    }

                    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                        override fun onQueryTextSubmit(query: String?): Boolean {
                            return false
                        }

                        override fun onQueryTextChange(query: String?): Boolean {
                            if (query.isNullOrEmpty()) {
                                submitList(list)
                            } else {
                                firestoreOperations.queryTodo(query, { list ->
                                    this@apply.submitList(list)
                                    binding.rvTodos.adapter = this@apply
                                }, { errorMessage ->
                                    requireView().showSnack(errorMessage)
                                })
                            }
                            return false
                        }
                    })
                }, {
                    requireView().showSnack(it)
                })
            }

            fabAddTodo.setOnClickListener {
                findNavController().navigate(R.id.todosToAddTodo)
            }
        }
    }

    private fun addNullToArray(data: List<Todo>): List<Todo?> {
        val newData = arrayListOf<Todo?>()
        for (i in data.indices) {
            if (i % 3 == 0) {
                if (i != 0 && i != data.size - 1)
                    newData.add(null)
            }
            newData.add(data[i])
        }
        return newData
    }
}