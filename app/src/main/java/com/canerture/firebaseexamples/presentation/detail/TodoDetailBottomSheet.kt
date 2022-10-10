package com.canerture.firebaseexamples.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.canerture.firebaseexamples.common.*
import com.canerture.firebaseexamples.common.Constants.PRIORITY_HIGH
import com.canerture.firebaseexamples.common.Constants.PRIORITY_LOW
import com.canerture.firebaseexamples.common.Constants.PRIORITY_MEDIUM
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

    @Inject
    lateinit var storageOperationsWrapper: StorageOperationsWrapper

    private var selectedPriority = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetTodoDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            radioButtonCheckedListener(rbLowPriority, rbMediumPriority, rbHighPriority) {
                selectedPriority = it
            }

            firestoreOperations.getTodoByDocumentIdOnce(args.documentId,
                onSuccess = { todoModel ->

                    todoModel.imageUrl?.let { imageUrl ->
                        imgTodoImage.visible()
                        imgDeleteImage.visible()
                        Glide.with(imgTodoImage).load(imageUrl).into(imgTodoImage)
                    }

                    etTodo.setText(todoModel.todo)

                    selectedPriority = todoModel.priority ?: PRIORITY_LOW

                    when (todoModel.priority) {
                        PRIORITY_LOW -> rbLowPriority.setCheckedTrue()
                        PRIORITY_MEDIUM -> rbMediumPriority.setCheckedTrue()
                        PRIORITY_HIGH -> rbHighPriority.setCheckedTrue()
                    }

                    btnUpdateData.setOnClickListener {

                        val todo = etTodo.text.toString()

                        if (todo.isNotEmpty()) {

                            firestoreOperations.updateTodo(todo, selectedPriority, args.documentId,
                                onSuccess = {
                                    this@TodoDetailBottomSheet.dismiss()
                                },
                                onFailure = {
                                    dialog?.showSnack(it)
                                })
                        } else {
                            dialog?.showSnack("Todo must not be empty!")
                        }
                    }

                    imgDeleteImage.setOnClickListener {

                        if (todoModel.imageName != null && todoModel.documentId != null) {

                            storageOperationsWrapper.deleteImage(todoModel.imageName,
                                onSuccess = {
                                    firestoreOperations.deleteImageFromTodo(todoModel.documentId,
                                        onSuccess = {
                                            imgTodoImage.gone()
                                            imgDeleteImage.gone()
                                        },
                                        onFailure = {
                                            dialog?.showSnack(it)
                                        })
                                },
                                onFailure = {
                                    dialog?.showSnack(it)
                                })
                        }
                    }
                },

                onFailure = {
                    dialog?.showSnack(it)
                })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}