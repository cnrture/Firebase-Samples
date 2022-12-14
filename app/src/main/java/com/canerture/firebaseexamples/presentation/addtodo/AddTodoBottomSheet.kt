package com.canerture.firebaseexamples.presentation.addtodo

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.canerture.firebaseexamples.common.*
import com.canerture.firebaseexamples.data.wrapper.FirestoreOperationsWrapper
import com.canerture.firebaseexamples.data.wrapper.StorageOperationsWrapper
import com.canerture.firebaseexamples.databinding.BottomSheetAddTodoBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddTodoBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetAddTodoBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var firestoreOperations: FirestoreOperationsWrapper

    @Inject
    lateinit var storageOperationsWrapper: StorageOperationsWrapper

    private var selectedPriority = "Low"

    private var selectedImageBitmap: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetAddTodoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            rbLowPriority.setCheckedTrue()

            radioButtonCheckedListener(rbLowPriority, rbMediumPriority, rbHighPriority) {
                selectedPriority = it
            }

            btnAddTodo.setOnClickListener {

                val todo = etTodo.text.toString()

                if (todo.isNotEmpty()) {
                    if (selectedImageBitmap != null) {
                        storageOperationsWrapper.addImage(selectedImageBitmap!!,
                            onSuccess = { imageUrl, imageName ->
                                addTodo(todo, imageUrl, imageName)
                            },
                            onFailure = {
                                dialog?.showSnack(it)
                            }
                        )
                    } else {
                        addTodo(todo, null, null)
                    }
                } else {
                    dialog?.showSnack("Todo must not be empty!")
                }
            }

            btnSetTodo.setOnClickListener {

                val todo = etTodo.text.toString()

                if (todo.isNotEmpty()) {
                    if (selectedImageBitmap != null) {
                        storageOperationsWrapper.addImage(selectedImageBitmap!!,
                            onSuccess = { imageUrl, imageName ->
                                setTodo(todo, imageUrl, imageName)
                            },
                            onFailure = {
                                dialog?.showSnack(it)
                            }
                        )
                    } else {
                        setTodo(todo, null, null)
                    }
                } else {
                    dialog?.showSnack("Todo must not be empty!")
                }
            }

            imgSelectImage.setOnClickListener {

                val intentToGallery =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)

                launcher.launch(intentToGallery)
            }
        }
    }

    private val launcher = this@AddTodoBottomSheet.resultLauncher(
        onSuccess = {
            binding.imgSelectImage.visible()
            binding.imgSelectImage.setImageBitmap(it)
            selectedImageBitmap = it
        },
        onFailure = {
            dialog?.showSnack(it)
            selectedImageBitmap = null
        }
    )

    private fun addTodo(todo: String, imageUrl: String?, imageName: String?) {
        firestoreOperations.addTodo(todo, selectedPriority, imageUrl, imageName,
            onSuccess = {
                this@AddTodoBottomSheet.dismiss()
            },
            onFailure = {
                dialog?.showSnack(it)
            }
        )
    }

    private fun setTodo(todo: String, imageUrl: String?, imageName: String?) {
        firestoreOperations.setTodo(todo, selectedPriority, imageUrl, imageName,
            onSuccess = {
                this@AddTodoBottomSheet.dismiss()
            },
            onFailure = {
                dialog?.showSnack(it)
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}