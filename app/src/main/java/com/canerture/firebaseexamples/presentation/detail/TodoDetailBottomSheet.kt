package com.canerture.firebaseexamples.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.canerture.firebaseexamples.common.*
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

            firestoreOperations.getTodoByDocumentIdOnce(args.documentId, { todoModel ->

                todoModel.imageUrl?.let { imageUrl ->
                    imgTodoImage.visible()
                    imgDeleteImage.visible()
                    Glide.with(imgTodoImage).load(imageUrl).into(imgTodoImage)
                }

                etTodo.setText(todoModel.todo)

                selectedPriority = todoModel.priority ?: Constants.PRIORITY_LOW

                when (todoModel.priority) {
                    Constants.PRIORITY_LOW -> rbLowPriority.setCheckedTrue()
                    Constants.PRIORITY_MEDIUM -> rbMediumPriority.setCheckedTrue()
                    Constants.PRIORITY_HIGH -> rbHighPriority.setCheckedTrue()
                }

                btnUpdateData.setOnClickListener {

                    val todo = etTodo.text.toString()

                    if (todo.isNotEmpty()) {
                        firestoreOperations.updateTodo(todo, selectedPriority, args.documentId, {
                            this@TodoDetailBottomSheet.dismiss()
                        }, {
                            showSnackBar(it)
                        })
                    } else {
                        showSnackBar("Todo must not be empty!")
                    }
                }

                imgDeleteImage.setOnClickListener {

                    if (todoModel.imageName != null && todoModel.documentId != null) {

                        storageOperationsWrapper.deleteImage(todoModel.imageName, {
                            firestoreOperations.deleteImageFromTodo(todoModel.documentId, {
                                imgTodoImage.gone()
                                imgDeleteImage.gone()
                            }, {
                                showSnackBar(it)
                            })
                        }, {
                            showSnackBar(it)
                        })
                    }
                }
            }, {
                showSnackBar(it)
            })
        }
    }

    private fun showSnackBar(text: String) {
        dialog?.window?.decorView?.showSnack(text)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}