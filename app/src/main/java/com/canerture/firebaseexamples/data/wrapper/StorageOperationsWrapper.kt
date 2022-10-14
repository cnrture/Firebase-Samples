package com.canerture.firebaseexamples.data.wrapper

import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*

class StorageOperationsWrapper(storage: FirebaseStorage) {

    private var imagesRef: StorageReference? = storage.reference.child("images")

    fun addImage(
        bitmap: Bitmap,
        onSuccess: (String, String) -> Unit = { _, _ -> },
        onFailure: (String) -> Unit
    ) {
        val uuid = UUID.randomUUID()
        val imageName = "$uuid.jpg"

        val reference = imagesRef?.child(imageName)

        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 75, byteArrayOutputStream)
        val data = byteArrayOutputStream.toByteArray()

        reference?.putBytes(data)?.addOnSuccessListener {
            it.storage.downloadUrl.addOnSuccessListener { uri ->
                onSuccess(uri.toString(), imageName)
            }.addOnFailureListener { exception ->
                onFailure(exception.message.orEmpty())
            }
        }?.addOnFailureListener {
            onFailure(it.message.orEmpty())
        }
    }

    fun downloadImage(
        imageName: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {

        val file =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES + File.separator)

        if (file == null) {
            onFailure("Failed to get external storage public directory")
        } else if (file.exists() && !file.isDirectory) {
            onFailure("${file.absolutePath} - already exists and is not a directory")
        } else {
            val destinationUri = Uri.withAppendedPath(Uri.fromFile(file), imageName)

            val downRef = imagesRef?.child(imageName)

            downRef?.getFile(destinationUri)?.addOnSuccessListener {
                onSuccess()
            }?.addOnFailureListener {
                onFailure(it.message.orEmpty())
            }
        }
    }

    fun deleteImage(
        imageName: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        val reference = imagesRef?.child(imageName)

        reference?.delete()
            ?.addOnSuccessListener { onSuccess() }
            ?.addOnFailureListener { onFailure(it.message.orEmpty()) }
    }

}