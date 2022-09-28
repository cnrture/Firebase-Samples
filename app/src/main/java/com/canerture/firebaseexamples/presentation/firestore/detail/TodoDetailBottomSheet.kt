package com.canerture.firebaseexamples.presentation.firestore.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.canerture.firebaseexamples.common.FirestoreOperationsWrapper
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetTodoDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            firestoreOperations.getTodoByDocumentId(args.documentId, {
                etTodo.setText(it.todo)
            }, {
                requireView().showSnack(it)
            })

            btnUpdateData.setOnClickListener {

                val todo = etTodo.text.toString()

                if (todo.isNotEmpty()) {
                    firestoreOperations.updateTodo(todo, args.documentId, {
                        requireView().showSnack("Todo updated!")
                    }, {
                        requireView().showSnack(it)
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