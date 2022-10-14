package com.canerture.firebaseexamples.presentation.todos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.canerture.firebaseexamples.R
import com.canerture.firebaseexamples.common.showLogDebug
import com.canerture.firebaseexamples.common.showSnack
import com.canerture.firebaseexamples.data.model.Todo
import com.canerture.firebaseexamples.data.wrapper.AdsOperationsWrapper
import com.canerture.firebaseexamples.data.wrapper.FirestoreOperationsWrapper
import com.canerture.firebaseexamples.databinding.FragmentTodosBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TodosFragment : Fragment() {

    private var _binding: FragmentTodosBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var firestoreOperations: FirestoreOperationsWrapper

    @Inject
    lateinit var adsOperationsWrapper: AdsOperationsWrapper

    private val todosAdapter by lazy { TodosAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTodosBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            rvTodos.adapter = todosAdapter

            firestoreOperations.getNotDoneTodosRealtime({ list ->

                adsOperationsWrapper.loadNativeAds(
                    onLoadedAd = {
                        todosAdapter.setNativeAds(it)
                        todosAdapter.submitList(addNullToArray(list))
                    },
                    onAdFailedToLoad = {
                        todosAdapter.submitList(list)
                        showLogDebug("NativeAds", it)
                    })

                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(query: String?): Boolean {
                        if (query.isNullOrEmpty()) {
                            todosAdapter.submitList(list)
                        } else {
                            firestoreOperations.queryTodo(query,
                                onSuccess = { list ->
                                    todosAdapter.submitList(list)
                                },
                                onFailure = { errorMessage ->
                                    view.showSnack(errorMessage)
                                }
                            )
                        }
                        return false
                    }
                })
            }, {
                view.showSnack(it)
            })

            todosAdapter.onDoneClick = { documentId ->
                firestoreOperations.setDone(documentId,
                    onSuccess = {
                        view.showSnack("Done State Updated!")
                    },
                    onFailure = {
                        view.showSnack(it)
                    }
                )
            }

            todosAdapter.onEditClick = { documentId ->
                val action = TodosFragmentDirections.todosToDetail(documentId)
                findNavController().navigate(action)
            }

            todosAdapter.onDeleteClick = { documentId ->
                firestoreOperations.deleteTodo(documentId,
                    onSuccess = {
                        view.showSnack("Data deleted!")
                    },
                    onFailure = {
                        view.showSnack(it)
                    }
                )
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}