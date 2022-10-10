package com.canerture.firebaseexamples.presentation.todos

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.canerture.firebaseexamples.R
import com.canerture.firebaseexamples.common.*
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

            TodosAdapter(
                onDoneClick = { documentId ->
                    firestoreOperations.setDone(documentId,
                        onSuccess = {
                            requireView().showSnack("Done State Updated!")
                        },
                        onFailure = {
                            requireView().showSnack(it)
                        }
                    )
                },

                onEditClick = { documentId ->
                    val action = TodosFragmentDirections.todosToDetail(documentId)
                    findNavController().navigate(action)
                },

                onDeleteClick = { documentId ->
                    firestoreOperations.deleteTodo(documentId,
                        onSuccess = {
                            requireView().showSnack("Data deleted!")
                        },
                        onFailure = {
                            requireView().showSnack(it)
                        }
                    )
                }
            ).apply {

                firestoreOperations.getNotDoneTodosRealtime({ list ->

                    adsOperationsWrapper.loadNativeAds(requireContext(),
                        onLoadedAd = {
                            setNativeAds(it)
                            submitList(addNullToArray(list))
                            binding.rvTodos.adapter = this
                        },
                        onAdFailedToLoad = {
                            submitList(list)
                            showLogDebug("NativeAds", it)
                        })

                    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                        override fun onQueryTextSubmit(query: String?): Boolean {
                            return false
                        }

                        override fun onQueryTextChange(query: String?): Boolean {
                            if (query.isNullOrEmpty()) {
                                submitList(list)
                            } else {
                                firestoreOperations.queryTodo(query,
                                    onSuccess = { list ->
                                        this@apply.submitList(list)
                                        binding.rvTodos.adapter = this@apply
                                    },
                                    onFailure = { errorMessage ->
                                        requireView().showSnack(errorMessage)
                                    }
                                )
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