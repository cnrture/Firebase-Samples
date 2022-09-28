package com.canerture.firebaseexamples.presentation.firestore.alltodos

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.canerture.firebaseexamples.R
import com.canerture.firebaseexamples.common.FirestoreOperationsWrapper
import com.canerture.firebaseexamples.common.showSnack
import com.canerture.firebaseexamples.common.viewBinding
import com.canerture.firebaseexamples.databinding.FragmentAllTodosBinding
import com.canerture.firebaseexamples.presentation.firestore.TodosAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AllTodosFragment : Fragment(R.layout.fragment_all_todos) {

    private val binding by viewBinding(FragmentAllTodosBinding::bind)

    @Inject
    lateinit var firestoreOperations: FirestoreOperationsWrapper

    private val todosAdapter by lazy { TodosAdapter() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listenTodos()
    }

    private fun listenTodos() {

        firestoreOperations.getTodosWithRealtimeUpdates({ list ->
            todosAdapter.updateList(list)
            binding.rvTodos.adapter = todosAdapter
        }, {
            requireView().showSnack(it)
        })
    }
}