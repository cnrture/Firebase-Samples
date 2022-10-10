package com.canerture.firebaseexamples.data.wrapper

import android.graphics.Bitmap
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream
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
            }
        }?.addOnFailureListener {
            onFailure(it.message.orEmpty())
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